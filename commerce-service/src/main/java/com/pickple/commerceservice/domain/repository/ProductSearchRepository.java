package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, UUID> {
    // Retrieve all products where isDelete is false and isPublic is true
    Page<ProductDocument> findAllByIsDeleteIsFalseAndIsPublicIsTrue(Pageable pageable);

    // Retrieve a product by productId where isDelete is false and isPublic is true
    Optional<ProductDocument> findByProductIdAndIsDeleteIsFalseAndIsPublicIsTrue(UUID productId);

    // Retrieve a product by productId where isDelete is false
    Optional<ProductDocument> findByProductIdAndIsDeleteIsFalse(UUID productId);

    Page<ProductDocument> findByProductNameContainingAndIsDeleteIsFalseAndIsPublicIsTrue(String keyword, Pageable pageable);

}
