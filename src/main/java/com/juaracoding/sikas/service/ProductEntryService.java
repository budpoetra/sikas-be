package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 12:07 AM
@Last Modified 12/4/2025 12:07 AM
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.model.ProductEntry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ProductEntryService {
    ResponseEntity<ApiResponse<Object>> addProductEntry(ProductEntry productEntry, HttpServletRequest request);
}
