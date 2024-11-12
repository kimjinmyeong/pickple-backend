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
public class ProductUpdatedEvent {
    private UUID productId;
    private String productName;
    private String description;
    private BigDecimal productPrice;
    private String productImage;
    private Boolean isPublic;
}
