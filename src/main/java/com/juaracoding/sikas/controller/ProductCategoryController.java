package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 11:52
@Last Modified 11/23/2025 11:52
Version 1.0
*/

import com.juaracoding.sikas.dto.validation.ProductCategoryDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.ProductCategoryResponse;
import com.juaracoding.sikas.service.ProductCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class ProductCategoryController {

        private final ProductCategoryService service;

        @PostMapping
        public ResponseEntity<ApiResponse<Object>> create(
                @Valid @RequestBody ProductCategoryDTO request
        ) {
            ProductCategoryResponse saved = service.create(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Category created successfully",
                            HttpStatus.CREATED.value(), saved));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<Object>> update(
                @PathVariable Long id,
                @Valid @RequestBody ProductCategoryDTO request
        ) {
            ProductCategoryResponse updated = service.update(id, request);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Category updated successfully",
                            HttpStatus.OK.value(), updated)
            );
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {

            service.delete(id);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Category deleted successfully",
                            HttpStatus.OK.value(), null)
            );
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<Object>> getOne(@PathVariable Long id) {

            ProductCategoryResponse found = service.getOne(id);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Category fetched successfully",
                            HttpStatus.OK.value(), found)
            );
        }

        @GetMapping
        public ResponseEntity<ApiResponse<Object>> getAll() {

            var list = service.getAll();

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "All categories fetched successfully",
                            HttpStatus.OK.value(), list)
            );
        }
    }




