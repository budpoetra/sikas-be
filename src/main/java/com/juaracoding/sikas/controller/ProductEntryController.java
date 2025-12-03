package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 12:45 AM
@Last Modified 12/4/2025 12:45 AM
Version 1.0
*/

import com.juaracoding.sikas.annotation.Loggable;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.ProductEntryDTO;
import com.juaracoding.sikas.model.ProductEntry;
import com.juaracoding.sikas.service.ProductEntryService;
import com.juaracoding.sikas.util.DtoToModelUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/product-entries")
@RequiredArgsConstructor
public class ProductEntryController {

    private final ProductEntryService productEntryService;

    @Loggable
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> addProductEntry(
            @Valid @RequestBody ProductEntryDTO dto,
            HttpServletRequest request
    ) {
        ProductEntry productEntry = DtoToModelUtil.map(dto, ProductEntry.class);
        return productEntryService.addProductEntry(productEntry, request);
    }
}