package com.pickple.payment_service.application.service;

import com.pickple.payment_service.exception.PaymentErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.payment_service.application.dto.PaymentRespDto;
import com.pickple.payment_service.domain.model.Payment;
import com.pickple.payment_service.domain.repository.PaymentRepository;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCancelFailureEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCancelResponseEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCreateFailureEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCreateResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventService paymentEventService;

    // 결제 생성
    @Transactional
    public void createPayment(UUID orderId, String userName, BigDecimal amount) {
        // Kafka 메세지 검증
        if(orderId == null || userName == null || amount == null) {
            throw new CustomException(PaymentErrorCode.INVALID_MESSAGE_FORMAT);
        }

        Payment payment = new Payment(orderId, userName, amount);

        try {
            // 결제 생성 후 대기 처리
            paymentRepository.save(payment);
        } catch(Exception e){
            PaymentCreateFailureEvent event = new PaymentCreateFailureEvent(orderId);
            paymentEventService.sendCreateFailureEvent(event);
            throw new CustomException(PaymentErrorCode.PAYMENT_CREATE_FAILED);
        }

        // 결제 완료 처리
        payment.success();
        paymentRepository.save(payment);

        PaymentCreateResponseEvent event = new PaymentCreateResponseEvent(payment.getOrderId(), payment.getPaymentId());
        paymentEventService.sendCreateSuccessEvent(event);

    }

    // 결제 취소 처리
    @Transactional
    public void cancelPayment(UUID orderId){
        // Kafka 메세지 검증
        if(orderId == null) {
            throw new CustomException(PaymentErrorCode.INVALID_MESSAGE_FORMAT);
        }

        Payment payment = paymentRepository.findByOrderIdAndIsDeleteIsFalse(orderId).orElseThrow(
                () -> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        try {
            // 결제 취소
            payment.canceled();
            paymentRepository.save(payment);
        } catch(Exception e){
            PaymentCancelFailureEvent event = new PaymentCancelFailureEvent(orderId);
            paymentEventService.sendCancelFailureEvent(event);
            throw new CustomException(PaymentErrorCode.PAYMENT_CANCEL_FAILED);
        }

        PaymentCancelResponseEvent event = new PaymentCancelResponseEvent(payment.getOrderId(), payment.getPaymentId());
        paymentEventService.sendCancelSuccessEvent(event);
    }

    // 결제 단건 조회
    @Transactional(readOnly = true)
    public PaymentRespDto getPaymentDetails (UUID paymentId){
        Payment payment = paymentRepository.findByPaymentIdAndIsDeleteIsFalse(paymentId).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        return PaymentRespDto.from(payment);
    }

    // 결제 전체 조회 (user)
    @Transactional(readOnly = true)
    public Page<PaymentRespDto> getPaymentsByUser(String userName, Pageable pageable) {
        Page<Payment> paymentList = paymentRepository.findAllByUserNameAndIsDeleteIsFalse(userName, pageable).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        return paymentList.map(PaymentRespDto::from);
    }

    // 결제 전체 조회 (admin)
    @Transactional(readOnly = true)
    public Page<PaymentRespDto> getPaymentByAdmin(Pageable pageable) {
        Page<Payment> paymentList = paymentRepository.findAllByIsDeleteIsFalse(pageable).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        return paymentList.map(PaymentRespDto::from);
    }



}
