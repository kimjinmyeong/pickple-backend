package com.pickple.commerceservice.application.service.command;

import com.pickple.commerceservice.application.dto.*;
import com.pickple.commerceservice.application.service.OrderMessagingProducerService;
import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.PreOrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.facade.RedissonLockStockFacade;
import com.pickple.commerceservice.infrastructure.feign.DeliveryClient;
import com.pickple.commerceservice.infrastructure.feign.PaymentClient;
import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.commerceservice.infrastructure.redis.TemporaryStorageService;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.PreOrderRequestDto;
import com.pickple.common_module.exception.CustomException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final StockService stockService;
    private final RedissonLockStockFacade redissonLockStockFacade;
    private final TemporaryStorageService temporaryStorageService;
    private final OrderMessagingProducerService messagingProducerService;
    private final ProductRepository productRepository;
    private final PreOrderRepository preOrderRepository;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    /**
     * 주문 생성
     */
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto, String username, String role) {
        // 주문 정보 생성
        Order order = Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .username(username)
                .build();

        // OrderDetail 정보 생성
        List<OrderDetail> orderDetails = requestDto.getOrderDetails().stream()
                .map(detail -> {
                    Product product = productRepository.findById(detail.getProductId())
                            .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));

                    // 재고 확인
                    StockByProductDto stockDto = stockService.getStockByProductId(product.getProductId());
                    if (stockDto.getStockQuantity() < detail.getOrderQuantity()) {
                        throw new CustomException(CommerceErrorCode.INSUFFICIENT_STOCK);
                    }

                    OrderDetail orderDetail = OrderDetail.builder()
                            .order(order)
                            .product(product)
                            .orderQuantity(detail.getOrderQuantity())
                            .unitPrice(product.getProductPrice())
                            .build();

                    orderDetail.calculateTotalPrice(); // 단가*수량 계산
                    return orderDetail;
                })
                .collect(Collectors.toList());

        order.addOrderDetails(orderDetails);
        order.calculateTotalAmount();

        orderRepository.save(order);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // 결제 요청 (Kafka)
                messagingProducerService.sendPaymentRequest(order.getOrderId(), order.getAmount(), username);

                // 주문 완료 알림 전송
                messagingProducerService.sendNotificationCreateRequest(
                        username,
                        role,
                        "System",
                        "주문 완료",
                        "주문이 성공적으로 완료되었습니다. 주문 번호: " + order.getOrderId(),
                        "ORDER"
                );
            }
        });

        // 배송 정보 저장 (Redis)
        temporaryStorageService.storeDeliveryInfoWithTTL(order.getOrderId(), requestDto.getDeliveryInfo());

        // OrderCreateResponseDto 반환 (fromEntity 메서드 활용)
        return OrderCreateResponseDto.fromEntity(order);
    }

    /**
     * 예약 구매 주문 생성
     */
    @Transactional
    public void createPreOrder(PreOrderRequestDto requestDto, String username) {
        // 상품 조회
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));

        // 예약구매 상품 여부 확인
        preOrderRepository.findByProduct_ProductIdAndIsDeleteFalse(product.getProductId())
                .orElseThrow(() -> new CustomException(CommerceErrorCode.INVALID_PRODUCT_FOR_PREORDER));

        // 재고 확인 및 차감
        try {
            redissonLockStockFacade.decreaseStockQuantityWithLock(product.getProductId());
        } catch (CustomException e) {
            log.error("주문 생성 실패: 재고가 부족합니다.");
            throw e;
        }

        // 주문 및 주문 정보 생성
        Order order = Order.builder()
                .username(username)
                .amount(product.getProductPrice())     // 주문 총액은 곧 상품 가격
                .build();

        // 주문 상태 변경
        order.changeStatus(OrderStatus.COMPLETED);

        // 주문 저장
        orderRepository.save(order);
    }

    /**
     * 주문 취소 메소드
     */
    @Transactional
    public OrderResponseDto cancelOrder(UUID orderId, String username, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        order.changeStatus(OrderStatus.CANCELED);
        order.markAsDeleted();
        orderRepository.save(order);

        // 결제 취소 요청 전송 (Kafka)
        messagingProducerService.sendPaymentCancelRequest(orderId);

        // 배송 정보 삭제 요청 처리
        try {
            DeliveryClientDto deliveryInfo = deliveryClient.getDeliveryInfo(role, username, orderId).getData();
            if (deliveryInfo != null && deliveryInfo.getDeliveryId() != null) {
                messagingProducerService.sendDeliveryDeleteRequest(deliveryInfo.getDeliveryId(), orderId, username);
            }
        } catch (FeignException e) {
            // 배송 정보를 가져오지 못했을 경우 로깅 처리
            log.error("Failed to retrieve delivery info for orderId: {}", orderId, e);
            throw new CustomException(CommerceErrorCode.DELIVERY_SERVICE_ERROR);
        }

        // OrderResponseDto 반환 (fromEntity 메서드 활용)
        return OrderResponseDto.fromEntity(order, null, null);
    }

    /**
     * 주문 타임아웃 처리
     */
    @Transactional
    public void handleOrderTimeout(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        // 결제가 이루어지지 않았고
        if (order.getPaymentId() == null) {
            log.info("주문이 결제되지 않았으므로 취소 처리됩니다. orderId: {}", orderId);

            // 결제 취소 요청 전송 (Kafka)
            messagingProducerService.sendPaymentCancelRequest(orderId);

            // 주문 상태를 CANCELED로 변경
            order.changeStatus(OrderStatus.CANCELED);
            order.markAsDeleted();
            orderRepository.save(order);
        }
    }
}