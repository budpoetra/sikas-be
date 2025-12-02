package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:12
@Last Modified 11/23/2025 19:12
Version 1.0
*/

import com.juaracoding.sikas.dto.validation.ProductDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.ProductResponse;
import com.juaracoding.sikas.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(
            @Valid @RequestBody ProductDTO request
    ) {
        ProductResponse saved = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Product created successfully",
                        HttpStatus.CREATED.value(),
                        saved
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO request
    ) {
        ProductResponse updated = service.update(id, request);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Product updated successfully",
                HttpStatus.OK.value(),
                updated
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Product deleted successfully",
                HttpStatus.OK.value(),
                null
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getOne(@PathVariable Long id) {

        ProductResponse found = service.getOne(id);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Product fetched successfully",
                HttpStatus.OK.value(),
                found
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAll() {

        var list = service.getAll();

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "All products fetched successfully",
                HttpStatus.OK.value(),
                list
        ));
    }
}



