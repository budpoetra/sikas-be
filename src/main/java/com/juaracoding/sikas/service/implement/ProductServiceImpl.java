package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:11
@Last Modified 11/23/2025 19:11
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.ProductDTO;
import com.juaracoding.sikas.dto.response.ProductCategoryResponse;
import com.juaracoding.sikas.dto.response.ProductResponse;
import com.juaracoding.sikas.model.Product;
import com.juaracoding.sikas.model.ProductCategory;
import com.juaracoding.sikas.repository.ProductCategoryRepository;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.service.ProductService;
import com.juaracoding.sikas.util.ResponseFactory;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    /**
     * Create new user
     * Platform Code: PDT
     * Module Code: 004
     * Quota Code: 01 - 10
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> create(ProductDTO request) {

        try {
            ProductCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> {
                        log.warn("PDT004W01 - Category with id {} not found", request.getCategoryId());

                        return new RuntimeException("PDT004W01 - Category not found");
                    });

            Product product = new Product();
            product.setProductName(request.getProductName());
            product.setProductCode(request.getProductCode());
            product.setPrice(request.getPrice());
            product.setBarcode(request.getBarcode());
            product.setStock(request.getStock());
            product.setCategory(category);
            product.setStatus(request.getStatus());
            product.setCreatedBy(1L);
            product.setUpdatedBy(1L);

            productRepository.save(product);

            return ResponseFactory.success(
                    "Product created successfully",
                    HttpStatus.CREATED,
                    toResponse(product)
            );
        } catch (Exception e) {
            log.error("PDT004W10 - Product not found");

            ApiResponse<Object> response = new ApiResponse<>(
                    false,
                    "PDT004W10 - Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    /**
     * Update existing user
     * Platform Code: PDT
     * Module Code: 004
     * Quota Code: 11 - 20
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> update(Long id, ProductDTO request) {

        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("PDT004W01 - Product with id {} not found", id);

                        return new RuntimeException("PDT004W01 - Product not found");
                    });

            ProductCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> {
                        log.warn("PDT004W02 - Category with id {} not found", request.getCategoryId());

                        return new RuntimeException("PDT004W02 - Category not found");
                    });

            product.setProductName(request.getProductName());
            product.setBarcode(request.getBarcode());
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());
            product.setCategory(category);
            product.setStatus(request.getStatus());
            product.setUpdatedBy(1L);


            productRepository.save(product);

            return ResponseFactory.success(
                    "Product updated successfully",
                    HttpStatus.OK,
                    toResponse(product)
            );
        } catch (Exception e) {
            log.error("PDT004E20 - Internal Server Error");

            ApiResponse<Object> response = new ApiResponse<>(
                    false,
                    "PDT004E20 - Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    /**
     * Delete existing user
     * Platform Code: PDT
     * Module Code: 004
     * Quota Code: 21 - 30
     */
    @Override
    public void delete(Long id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("PDT004W21 - Product with id {} not found", id);

                        return new RuntimeException("PDT004W21 - Product not found");
                    });

            productRepository.delete(product);
        } catch (Exception e) {
            log.error("PDT004E30 - Internal Server Error");

            throw new RuntimeException("PDT004E30 - Internal Server Error");
        }
    }

    /**
     * Get one existing user
     * Platform Code: PDT
     * Module Code: 004
     * Quota Code: 31 - 40
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> getOne(Long id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("PDT004W31 - Product with id {} not found", id);

                        return new RuntimeException("PDT004W31 - Product not found");
                    });

            return ResponseFactory.success(
                    "Product fetched successfully",
                    HttpStatus.OK,
                    toResponse(product)
            );
        } catch (Exception e) {
            log.error("PDT004E40 - Internal Server Error");

            ApiResponse<Object> response = new ApiResponse<>(
                    false,
                    "PDT004E40 - Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    /**
     * Get all existing users
     * Platform Code: PDT
     * Module Code: 004
     * Quota Code: 41 - 50
     */
    @Override
    public List<ProductResponse> getAll() {
        try {
            return productRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .toList();
        } catch (Exception e) {
            log.error("PDT004E50 - Internal Server Error");

            throw new RuntimeException("PDT004E50 - Internal Server Error");
        }

    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .productName(p.getProductName())
                .productCode(p.getProductCode())
                .barcode(p.getBarcode())
                .price(p.getPrice())
                .status(p.getStatus())
                .stock(p.getStock())
                .createdDate(p.getCreatedDate())
                .updatedDate(p.getUpdatedDate())
                .createdBy(p.getCreatedBy())
                .updatedBy(p.getUpdatedBy())
                .category(
                        ProductCategoryResponse.builder()
                                .id(p.getCategory().getId())
                                .category(p.getCategory().getCategory())
                                .createdDate(p.getCategory().getCreatedDate())
                                .updatedDate(p.getCategory().getUpdatedDate())
                                .createdBy(p.getCategory().getCreatedBy())
                                .updatedBy(p.getCategory().getUpdatedBy())
                                .build()
                )
                .build();

}
}



