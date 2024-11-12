package com.pickple.commerceservice.infrastructure.messaging;

import com.pickple.commerceservice.application.service.ProductEventService;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductCreatedEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductDeletedEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductUpdatedEvent;
import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductMessagingConsumerService {

    private final ProductEventService productEventService;

    @KafkaListener(topics = "${kafka.topic.product-created}", groupId = "commerce-service")
    public void listenProductCreated(String message) {
        try {
            ProductCreatedEvent event = EventSerializer.deserialize(message, ProductCreatedEvent.class);
            productEventService.handleProductCreated(event);
        } catch (RuntimeException e) {
            log.error("Failed to deserialize ProductCreatedEvent: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "${kafka.topic.product-updated}", groupId = "commerce-service")
    public void listenProductUpdated(String message) {
        try {
            ProductUpdatedEvent event = EventSerializer.deserialize(message, ProductUpdatedEvent.class);
            productEventService.handleProductUpdated(event);
        } catch (RuntimeException e) {
            log.error("Failed to deserialize ProductUpdatedEvent: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "${kafka.topic.product-deleted}", groupId = "commerce-service")
    public void listenProductDeleted(String message) {
        try {
            ProductDeletedEvent event = EventSerializer.deserialize(message, ProductDeletedEvent.class);
            productEventService.handleProductDeleted(event);
        } catch (RuntimeException e) {
            log.error("Failed to deserialize ProductDeletedEvent: {}", e.getMessage());
        }
    }
}