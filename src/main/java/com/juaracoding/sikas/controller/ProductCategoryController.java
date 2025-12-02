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

import com.juaracoding.sikas.annotation.Loggable;
import com.juaracoding.sikas.dto.validation.ProductCategoryDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
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

        @Loggable
        @PostMapping
        public ResponseEntity<ApiResponse<Object>> create(
                @Valid @RequestBody ProductCategoryDTO request
        ) {
            return service.create(request);
        }

        @Loggable
        @PutMapping("/{id}")
        public ResponseEntity<ApiResponse<Object>> update(
                @PathVariable Long id,
                @Valid @RequestBody ProductCategoryDTO request
        ) {
            return service.update(id, request);
        }

        @Loggable
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {

            service.delete(id);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Category deleted successfully",
                            HttpStatus.OK.value(), null)
            );
        }

        @Loggable
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<Object>> getOne(@PathVariable Long id) {
            return service.getOne(id);
        }

        @Loggable
        @GetMapping
        public ResponseEntity<ApiResponse<Object>> getAll() {
            return ResponseEntity.ok(
                    new ApiResponse<>(true,
                            "List of all categories",
                            HttpStatus.OK.value(),
                            service.getAll())
            );
        }
    }




