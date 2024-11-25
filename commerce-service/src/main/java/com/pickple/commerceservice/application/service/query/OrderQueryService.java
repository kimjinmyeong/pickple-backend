package com.pickple.commerceservice.application.service.query;

import com.pickple.commerceservice.application.dto.OrderByVendorResponseDto;
import com.pickple.commerceservice.application.dto.OrderResponseDto;
import com.pickple.commerceservice.application.dto.OrderSummaryResponseDto;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.feign.DeliveryClient;
import com.pickple.commerceservice.infrastructure.feign.PaymentClient;
import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import com.pickple.common_module.exception.CustomException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    /**
     * 주문 단건 조회
     */
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID orderId, String role, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        // 결제 정보 가져오기
        PaymentClientDto paymentInfo = null;
        if (order.getPaymentId() != null) {
            try {
                paymentInfo = paymentClient.getPaymentInfo(role, username, orderId);
            } catch (FeignException e) {
                // 결제 정보를 가져오지 못했을 경우 CustomException 발생
                throw new CustomException(CommerceErrorCode.PAYMENT_SERVICE_ERROR);
            }
        }

        // 배송 정보 가져오기
        DeliveryClientDto deliveryInfo = null;
        if (order.getDeliveryId() != null) {
            try {
                deliveryInfo = deliveryClient.getDeliveryInfo(role, username, orderId).getData();
            } catch (FeignException e) {
                // 배송 정보를 가져오지 못했을 경우 CustomException 발생
                throw new CustomException(CommerceErrorCode.DELIVERY_SERVICE_ERROR);
            }
        }

        // OrderResponseDto 반환 (fromEntity 메서드 활용)
        return OrderResponseDto.fromEntity(order, paymentInfo, deliveryInfo);
    }

    /**
     * 업체별 주문 조회
     */
    @Transactional(readOnly = true)
    public Page<OrderByVendorResponseDto> findByVendorId(UUID vendorId, Pageable pageable) {
        return orderRepository.findOrdersByVendorId(vendorId, pageable)
                .map(OrderByVendorResponseDto::fromEntity);
    }

    /**
     * 전체 주문 조회
     */
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderSummaryResponseDto::fromEntity);
    }

    /**
     * 내 주문 조회
     */
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponseDto> getOrdersByUsername(String username, Pageable pageable) {
        return orderRepository.findByUsernameAndIsDeleteFalse(username, pageable)
                .map(OrderSummaryResponseDto::fromEntity);
    }

    /**
     * 주문 검색 (주문 상태)
     */
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponseDto> findOrdersByOrderStatus(OrderStatus orderStatus, Pageable pageable) {
        return orderRepository.findOrdersByOrderStatus(orderStatus, pageable)
                .map(OrderSummaryResponseDto::fromEntity);
    }

    /**
     * 배송아이디로 username 검색
     */
    @Transactional(readOnly = true)
    public String findUsernameByDeliveryId(UUID deliveryId) {
        return orderRepository.findByDeliveryId(deliveryId).orElseThrow(
                () -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND)
        ).getUsername();
    }
}