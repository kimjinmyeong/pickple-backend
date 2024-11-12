package com.pickple.commerceservice.application.service.command;

import com.pickple.commerceservice.application.mapper.ProductMapper;
import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.model.Vendor;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.domain.repository.VendorRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.ProductCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.ProductUpdateRequestDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final StockService stockService;

    // Create Product
    @Transactional
    public Product createProduct(ProductCreateRequestDto createDto) {
        Vendor vendor = vendorRepository.findByVendorIdAndIsDeleteFalse(createDto.getVendorId())
                .orElseThrow(() -> new CustomException(CommerceErrorCode.VENDOR_NOT_FOUND));

        Product savedProduct = productRepository.save(ProductMapper.toEntity(createDto, vendor));
        Stock savedStock = stockService.createStock(savedProduct, createDto.getStock().getStockQuantity());

        savedProduct.assignStock(savedStock);
        return productRepository.save(savedProduct);
    }

    // Update Product
    @Transactional
    public Product updateProduct(UUID productId, ProductUpdateRequestDto updateDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));
        product.update(updateDto);
        return product;
    }

    // Soft Delete Product
    @Transactional
    public Product softDeleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));
        product.markAsDeleted();
        return product;
    }
}