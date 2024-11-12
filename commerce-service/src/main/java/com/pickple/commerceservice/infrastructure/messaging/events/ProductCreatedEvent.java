package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreatedEvent {
    private UUID productId;
    private String productName;
    private String description;
    private BigDecimal productPrice;
    private String productImage;
    private Boolean isPublic;
    private Boolean isDelete;
    private VendorInfo vendor;
    private StockInfo stock;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorInfo {
        private UUID vendorId;
        private String vendorName;
        private String vendorAddress;
        private String username;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockInfo {
        private UUID stockId;
        private Long stockQuantity;
    }
}