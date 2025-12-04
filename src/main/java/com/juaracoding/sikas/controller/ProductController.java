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

import com.juaracoding.sikas.annotation.Loggable;
import com.juaracoding.sikas.dto.validation.ProductDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.group.*;
import com.juaracoding.sikas.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService service;

    @Loggable
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(
            @Validated(Create.class) @RequestBody ProductDTO request
    ) {
        return service.create(request);
    }

    @Loggable
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(
            @PathVariable Long id,
            @Validated(Update.class) @RequestBody ProductDTO request
    ) {
        return service.update(id, request);
    }

    @Loggable
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

    @Loggable
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getOne(@PathVariable Long id) {
        return service.getOne(id);
    }

    @Loggable
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getListProduct(@RequestParam(value = "search", required = false) String search,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @RequestParam(value = "sort", defaultValue = "id") String sort,
                                                           @RequestParam(value = "direction", defaultValue = "desc") String direction,
                                                           HttpServletRequest request) {
        return service.getListProduct(search, page, size, sort, direction, request);
    }
}



