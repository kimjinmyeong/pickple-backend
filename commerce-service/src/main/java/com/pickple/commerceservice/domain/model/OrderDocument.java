package com.pickple.commerceservice.domain.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Field;

@Document(value = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDocument {

    @Id
    @Field(name = "order_id")
    private UUID orderId;

    @Field("username")
    private String username;

    @Field("amount")
    private int amount;

    @Field("order_status")
    private String orderStatus;

    @Field("order_details")
    private List<OrderDetail> orderDetails;

    @Field("payment_info")
    private PaymentInfo paymentInfo;

    @Field("delivery_info")
    private DeliveryInfo deliveryInfo;

    @Getter
    @Builder
    public static class OrderDetail {

        @Field("product_id")
        private String productId;

        @Field("order_quantity")
        private int orderQuantity;

        @Field("total_price")
        private int totalPrice;
    }

    @Getter
    @Builder
    public static class PaymentInfo {

        @Field("order_id")
        private String orderId;

        @Field("payment_id")
        private String paymentId;

        @Field("amount")
        private int amount;

        @Field("method")
        private String method;

        @Field("status")
        private String status;
    }

    @Getter
    @Builder
    public static class DeliveryInfo {

        @Field("delivery_id")
        private String deliveryId;

        @Field("order_id")
        private String orderId;

        @Field("carrier_name")
        private String carrierName;

        @Field("delivery_type")
        private String deliveryType;

        @Field("tracking_number")
        private String trackingNumber;

        @Field("delivery_status")
        private String deliveryStatus;

        @Field("delivery_requirement")
        private String deliveryRequirement;

        @Field("recipient_name")
        private String recipientName;

        @Field("recipient_address")
        private String recipientAddress;

        @Field("recipient_contact")
        private String recipientContact;
    }
}