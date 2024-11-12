package com.pickple.commerceservice.application.dto;

import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.ProductDocument;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.model.Vendor;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private UUID productId;
    private String productName;
    private String description;
    private BigDecimal productPrice;
    private String productImage;
    private Boolean isPublic;
    private VendorInfo vendor;
    private StockInfo stock;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VendorInfo {
        private UUID vendorId;
        private String vendorName;
        private String vendorAddress;
        private String username;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockInfo {
        private UUID stockId;
        private Long stockQuantity;
    }

    public static ProductResponseDto fromEntity(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .productPrice(product.getProductPrice())
                .productImage(product.getProductImage())
                .isPublic(product.getIsPublic())
                .vendor(toVendorInfo(product.getVendor()))
                .stock(toStockInfo(product.getStock()))
                .build();
    }

    public static ProductResponseDto fromDocument(ProductDocument product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .productPrice(product.getProductPrice())
                .productImage(product.getProductImage())
                .isPublic(product.getIsPublic())
                .vendor(toVendorInfo(product.getVendor()))
                .stock(toStockInfo(product.getStock()))
                .build();
    }

    private static VendorInfo toVendorInfo(ProductDocument.VendorInfo vendor) {
        if (vendor == null) {
            return null;
        }
        return new VendorInfo(
                vendor.getVendorId(),
                vendor.getVendorName(),
                vendor.getVendorAddress(),
                vendor.getUsername()
        );
    }

    private static StockInfo toStockInfo(ProductDocument.StockInfo stock) {
        if (stock == null) {
            return null;
        }
        return new StockInfo(
                stock.getStockId(),
                stock.getStockQuantity()
        );
    }

    private static VendorInfo toVendorInfo(Vendor vendor) {
        if (vendor == null) {
            return null;
        }
        return new VendorInfo(
                vendor.getVendorId(),
                vendor.getVendorName(),
                vendor.getVendorAddress(),
                vendor.getUsername()
        );
    }

    private static StockInfo toStockInfo(Stock stock) {
        if (stock == null) {
            return null;
        }
        return new StockInfo(
                stock.getStockId(),
                stock.getStockQuantity()
        );
    }
}