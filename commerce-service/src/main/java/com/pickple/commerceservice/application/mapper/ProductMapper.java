package com.pickple.commerceservice.application.mapper;

import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.ProductDocument;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.model.Vendor;
import com.pickple.commerceservice.infrastructure.messaging.events.ProductCreatedEvent;
import com.pickple.commerceservice.presentation.dto.request.ProductCreateRequestDto;

public class ProductMapper {

    public static Product toEntity(ProductCreateRequestDto createDto, Vendor vendor) {
        return Product.builder()
                .productName(createDto.getProductName())
                .description(createDto.getDescription())
                .productPrice(createDto.getProductPrice())
                .productImage(createDto.getProductImage())
                .isPublic(createDto.getIsPublic())
                .vendor(vendor)
                .build();
    }

    public static ProductDocument toDocument(Product product) {
        return ProductDocument.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .productPrice(product.getProductPrice())
                .productImage(product.getProductImage())
                .isPublic(product.getIsPublic())
                .isDelete(product.getIsDelete())
                .vendor(toVendorInfo(product.getVendor()))
                .stock(toStockInfo(product.getStock()))
                .build();
    }

    public static ProductCreatedEvent toProductCreatedEvent(Product product, Vendor vendor, Stock stock) {
        return ProductCreatedEvent.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .productImage(product.getProductImage())
                .isPublic(product.getIsPublic())
                .isDelete(product.getIsDelete())
                .productPrice(product.getProductPrice())
                .vendor(new ProductCreatedEvent.VendorInfo(
                        vendor.getVendorId(),
                        vendor.getVendorName(),
                        vendor.getVendorAddress(),
                        vendor.getUsername()
                ))
                .stock(new ProductCreatedEvent.StockInfo(
                        stock.getStockId(),
                        stock.getStockQuantity()
                ))
                .build();
    }


    private static ProductDocument.VendorInfo toVendorInfo(Vendor vendor) {
        if (vendor == null) {
            return null;
        }
        return new ProductDocument.VendorInfo(
                vendor.getVendorId(),
                vendor.getVendorName(),
                vendor.getVendorAddress(),
                vendor.getUsername()
        );
    }

    private static ProductDocument.StockInfo toStockInfo(Stock stock) {
        if (stock == null) {
            return null;
        }
        return new ProductDocument.StockInfo(
                stock.getStockId(),
                stock.getStockQuantity()
        );
    }
}
