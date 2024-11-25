package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderMongoRepository extends MongoRepository<OrderDocument, UUID> {
}
