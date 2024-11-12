package com.pickple.commerceservice.application.service.query;

import com.pickple.commerceservice.application.dto.ProductResponseDto;
import com.pickple.commerceservice.application.dto.ProductSearchResDto;
import com.pickple.commerceservice.domain.model.ProductDocument;
import com.pickple.commerceservice.domain.repository.ProductSearchRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.ProductSearchReqDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductSearchRepository productRepository;

    // Get All Products
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<ProductDocument> products = productRepository.findAllByIsDeleteIsFalseAndIsPublicIsTrue(pageable);
        return products.map(ProductResponseDto::fromDocument);
    }

    // Get Product by ID
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID productId) {
        ProductDocument product = productRepository.findByProductIdAndIsDeleteIsFalseAndIsPublicIsTrue(productId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));
        return ProductResponseDto.fromDocument(product);
    }

    // Search Products
    @Transactional(readOnly = true)
    public Page<ProductSearchResDto> searchProducts(ProductSearchReqDto searchDto, Pageable pageable) {
        Page<ProductDocument> products = productRepository.findByProductNameContainingAndIsDeleteIsFalseAndIsPublicIsTrue(searchDto.getKeyword(), pageable);
        return products.map(ProductSearchResDto::fromDocument);
    }
}