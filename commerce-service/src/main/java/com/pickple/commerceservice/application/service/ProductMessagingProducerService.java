package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.infrastructure.messaging.events.ProductCreatedEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductDeletedEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductMessagingProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.product-created}")
    private String productCreatedTopic;

    @Value("${kafka.topic.product-updated}")
    private String productUpdatedTopic;

    @Value("${kafka.topic.product-deleted}")
    private String productDeletedTopic;

    public void sendProductCreatedEvent(ProductCreatedEvent event) {
        kafkaTemplate.send(productCreatedTopic, event);
    }

    public void sendProductUpdatedEvent(ProductUpdatedEvent event) {
        kafkaTemplate.send(productUpdatedTopic, event);
    }

    public void sendProductDeletedEvent(ProductDeletedEvent event) {
        kafkaTemplate.send(productDeletedTopic, event);
    }
}