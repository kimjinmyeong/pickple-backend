//package com.pickple.commerceservice.application.service;
//
//import com.pickple.commerceservice.application.mapper.ProductMapper;
//import com.pickple.commerceservice.domain.model.Product;
//import com.pickple.commerceservice.domain.model.ProductDocument;
//import com.pickple.commerceservice.domain.model.Stock;
//import com.pickple.commerceservice.domain.model.PreOrderDetails;
//import com.pickple.commerceservice.domain.repository.PreOrderRepository;
//import com.pickple.commerceservice.domain.repository.ProductRepository;
//import com.pickple.commerceservice.domain.repository.ProductSearchRepository;
//import com.pickple.commerceservice.domain.repository.StockRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class ProductDataInitializer implements CommandLineRunner {
//
//    private final ProductRepository productRepository;
//    private final StockRepository stockRepository;
//    private final PreOrderRepository preOrderDetailsRepository;
//    private final ProductSearchRepository productSearchRepository;
//
//    @Override
//    public void run(String... args) {
////        int page = 0;
////        int size = 100; // Batch size
////        Page<Product> productPage;
////        do {
////            productPage = productRepository.findAll(PageRequest.of(page, size));
////            List<ProductDocument> productDocuments = productPage.stream()
////                    .map(ProductMapper::toDocument)
////                    .collect(Collectors.toList());
////            productSearchRepository.saveAll(productDocuments); // Bulk save
////            page++;
////        } while (productPage.hasNext());
//        for (int i = 0; i < 212; i++) { // Adjust the count as needed
//            // Create Product
//            Product product = Product.builder()
//                    .productName("ProductTT " + i)
//                    .description("Description for product " + i)
//                    .productPrice(BigDecimal.valueOf(10.0 + i))
//                    .productImage("http://example.com/image" + i + ".png")
//                    .isPublic(true)
//                    .build();
//
//            productRepository.save(product);
//
//            // Create Stock for Product
//            Stock stock = Stock.builder()
//                    .stockQuantity(100L) // Example quantity
//                    .product(product)
//                    .build();
//
//            stockRepository.save(stock);
//            product.assignStock(stock); // Link stock to product
//
//            // Create PreOrderDetails for Product
//            PreOrderDetails preOrderDetails = PreOrderDetails.builder()
//                    .product(product)
//                    .preOrderStartDate(LocalDateTime.now())
//                    .preOrderEndDate(LocalDateTime.now().plusDays(30))
//                    .preOrderStockQuantity(50L) // Example pre-order stock quantity
//                    .build();
//
//            preOrderDetailsRepository.save(preOrderDetails);
//            productSearchRepository.save(ProductMapper.toDocument(product));
//        }
//    }
//}