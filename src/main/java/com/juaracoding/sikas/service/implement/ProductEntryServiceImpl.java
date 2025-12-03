package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 12:33 AM
@Last Modified 12/4/2025 12:33 AM
Version 1.0
*/

import com.juaracoding.sikas.model.Product;
import com.juaracoding.sikas.model.ProductEntry;
import com.juaracoding.sikas.repository.ProductEntryRepository;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.service.ProductEntryService;
import com.juaracoding.sikas.util.ResponseFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.ProductEntryDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEntryServiceImpl implements ProductEntryService {

    private final ProductRepository productRepository;
    private final ProductEntryRepository productEntryRepository;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> addProductEntry(ProductEntry productEntry, HttpServletRequest request) {

        try {
            Product product = productRepository.findById(productEntry.getProductId())
                    .orElseThrow(() -> {
                        log.error("Product with ID {} not found", productEntry.getProductId());
                        return new RuntimeException("Product with ID " + productEntry.getProductId() + " not found");
                    });

            long newStock = product.getStock() + productEntry.getQty();
            product.setStock(Math.toIntExact(newStock));
            productRepository.save(product);

            ProductEntry newProductEntry = ProductEntry.builder()
                    .productId(productEntry.getProductId())
                    .qty(productEntry.getQty())
                    .build();
            productEntryRepository.save(newProductEntry);

            return ResponseFactory.success(
                    "Successfully added " + productEntry.getQty() + " to product ID " + productEntry.getProductId(),
                    HttpStatus.OK,
                    newProductEntry
            );
        } catch (Exception e) {
            log.error("Error adding product entry: {}", e.getMessage());
            return ResponseFactory.error(
                    "Failed to add product entry: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }

    }

}