package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.ProductResponseDto;
import com.pickple.commerceservice.application.dto.ProductSearchResDto;
import com.pickple.commerceservice.application.mapper.ProductMapper;
import com.pickple.commerceservice.application.service.command.ProductCommandService;
import com.pickple.commerceservice.application.service.query.ProductQueryService;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.model.Vendor;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductCreatedEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductDeletedEvent;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductUpdatedEvent;
import com.pickple.commerceservice.presentation.dto.request.ProductCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.ProductSearchReqDto;
import com.pickple.commerceservice.presentation.dto.request.ProductUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;
    private final ProductMessagingProducerService productMessagingProducerService;

    // 상품 생성
    public ProductResponseDto createProduct(ProductCreateRequestDto createDto) {
        Product savedProduct = productCommandService.createProduct(createDto);
        Vendor vendor = savedProduct.getVendor();
        Stock stock = savedProduct.getStock();

        // Send ProductCreatedEvent
        ProductCreatedEvent event = ProductMapper.toProductCreatedEvent(savedProduct, vendor, stock);
        productMessagingProducerService.sendProductCreatedEvent(event);
        return ProductResponseDto.fromEntity(savedProduct);
    }

    // 상품 전체 조회
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productQueryService.getAllProducts(pageable);
    }

    // 상품 상세 조회
    public ProductResponseDto getProductById(UUID productId) {
        return productQueryService.getProductById(productId);
    }

    // 상품 수정
    public ProductResponseDto updateProduct(UUID productId, ProductUpdateRequestDto updateDto) {
        Product updatedProduct = productCommandService.updateProduct(productId, updateDto);

        ProductUpdatedEvent event = ProductUpdatedEvent.builder()
                .productId(updatedProduct.getProductId())
                .productName(updatedProduct.getProductName())
                .description(updatedProduct.getDescription())
                .productImage(updatedProduct.getProductImage())
                .productPrice(updatedProduct.getProductPrice())
                .isPublic(updatedProduct.getIsPublic())
                .build();
        productMessagingProducerService.sendProductUpdatedEvent(event);
        return ProductResponseDto.fromEntity(updatedProduct);
    }

    // 상품 삭제
    public void softDeleteProduct(UUID productId) {
        Product deletedProduct = productCommandService.softDeleteProduct(productId);

        ProductDeletedEvent event = new ProductDeletedEvent(deletedProduct.getProductId());
        productMessagingProducerService.sendProductDeletedEvent(event);
    }

    public Page<ProductSearchResDto> searchProducts(ProductSearchReqDto searchDto, Pageable pageable) {
        return productQueryService.searchProducts(searchDto, pageable);
    }
}
