package com.pickple.commerceservice.infrastructure.messaging.events;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDeletedEvent {
    private UUID productId;
}