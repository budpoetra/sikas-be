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

import com.juaracoding.sikas.dto.LinksDTO;
import com.juaracoding.sikas.dto.MetaDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.PageResponse;
import com.juaracoding.sikas.dto.validation.ProductDTO;
import com.juaracoding.sikas.dto.response.ProductResponse;
import com.juaracoding.sikas.model.Product;
import com.juaracoding.sikas.repository.ProductCategoryRepository;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.service.ProductService;
import com.juaracoding.sikas.util.PaginationUrlBuilderUtil;
import com.juaracoding.sikas.util.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

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
            categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> {
                        log.warn("PDT004W01 - Category with id {} not found", request.getCategoryId());

                        return new RuntimeException("PDT004W01 - Category not found");
                    });

            if (productRepository.existsByProductCode(request.getProductCode())) {
                log.warn("PDT004W02 - Duplicate productCode {}", request.getProductCode());

                throw new RuntimeException("PDT004W02 - Product code already exists");
            }

            String barcode = BarcodeGenerator.generateEAN13();

            Product product = Product.builder()
                    .productName(request.getProductName())
                    .productCode(request.getProductCode())
                    .price(request.getPrice())
                    .barcode(barcode)
                    .stock(0)
                    .categoryId(request.getCategoryId())
                    .status(request.getStatus() != null ? request.getStatus() : 1)
                    .build();

            productRepository.save(product);

            return ResponseFactory.success(
                    "Product created successfully",
                    HttpStatus.CREATED,
                    toResponse(product)
            );
        } catch (Exception e) {
            log.error("PDT004W10 - Internal Server Error : {}", e.getMessage());

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
            if (!categoryRepository.existsById(request.getCategoryId())) {
                log.warn("PDT004W11 - Category with id {} not found", request.getCategoryId());

                throw new RuntimeException("PDT004W12 - Category not found");
            }

            Product product = productRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("PDT004W11 - Product with id {} not found", id);

                        return new RuntimeException("PDT004W01 - Product not found");
                    });

            product.setProductName(request.getProductName());
            product.setPrice(request.getPrice());
            product.setCategoryId(request.getCategoryId());

            productRepository.save(product);

            return ResponseFactory.success(
                    "Product updated successfully",
                    HttpStatus.OK,
                    toResponse(product)
            );
        } catch (Exception e) {
            log.error("PDT004E20 - Internal Server Error : {}", e.getMessage());

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
            Optional<Product> productOpt = productRepository.findById(id);
            if (productOpt.isEmpty()) {
                log.warn("PDT004W21 - Product with id {} not found", id);

                throw new RuntimeException("PDT004W21 - Product not found");
            }

            Product product = productOpt.get();

            product.setStatus(0);
            productRepository.save(product);
        } catch (Exception e) {
            log.error("PDT004E30 - Internal Server Error : {}", e.getMessage());

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
    public ResponseEntity<ApiResponse<Object>> getListProduct(
            String search,
            int page,
            int size,
            String sort,
            String direction,
            HttpServletRequest request
    ) {
        try {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    direction.equalsIgnoreCase("desc")
                            ? Sort.by(sort).descending()
                            : Sort.by(sort).ascending()
            );

            Page<Product> productPage = productRepository.searchListProduct(search, pageable);

            List<ProductResponse> content = productPage.stream()
                    .map(this::toResponse)
                    .toList();

            int totalPages = productPage.getTotalPages();

            String selfUrl = PaginationUrlBuilderUtil.build(request, page);
            String nextUrl = PaginationUrlBuilderUtil.buildNullable(request, page + 1, totalPages);
            String prevUrl = PaginationUrlBuilderUtil.buildNullable(request, page - 1, totalPages);

            MetaDTO meta = new MetaDTO(
                    productPage.getNumber() + 1,
                    productPage.getSize(),
                    productPage.getTotalElements(),
                    productPage.getTotalPages()
            );

            LinksDTO links = new LinksDTO(
                    selfUrl,
                    nextUrl,
                    prevUrl
            );

            PageResponse<Object> response = new PageResponse<>(
                    content,
                    meta,
                    links
            );

            return ResponseFactory.success(
                    "Product list retrieved successfully",
                    HttpStatus.OK,
                    response
            );
        } catch (Exception e) {
            log.error("PDT004E50 - Internal Server Error");

            ApiResponse<Object> response = new ApiResponse<>(
                    false,
                    "PDT004E50 - Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    private static class BarcodeGenerator {

        private static final SecureRandom random = new SecureRandom();

        public static String generateEAN13() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 12; i++) {
                sb.append(random.nextInt(10)); // 0 - 9
            }

            String base12 = sb.toString();

            int checkDigit = calculateCheckDigit(base12);

            return base12 + checkDigit;
        }

        private static int calculateCheckDigit(String base12) {
            int sum = 0;

            for (int i = 0; i < 12; i++) {
                int digit = base12.charAt(i) - '0';

                if ((i % 2) == 0) {
                    sum += digit;
                } else {
                    sum += digit * 3;
                }
            }

            int mod = sum % 10;
            return (mod == 0) ? 0 : (10 - mod);
        }
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .productName(p.getProductName())
                .productCode(p.getProductCode())
                .categoryId(p.getCategoryId())
                .barcode(p.getBarcode())
                .price(p.getPrice())
                .status(p.getStatus())
                .stock(p.getStock())
                .createdDate(p.getCreatedDate())
                .updatedDate(p.getUpdatedDate())
                .createdBy(p.getCreatedBy())
                .updatedBy(p.getUpdatedBy())
                .build();
    }
}



