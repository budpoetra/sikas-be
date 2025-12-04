package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 11:51
@Last Modified 11/23/2025 11:51
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.ProductCategoryDTO;
import com.juaracoding.sikas.dto.response.ProductCategoryResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Platform Code: PDC
 * Module Code: 005
 */
public interface ProductCategoryService {
    ResponseEntity<ApiResponse<Object>> create(ProductCategoryDTO request);
    ResponseEntity<ApiResponse<Object>> update(Long id, ProductCategoryDTO request);
    void delete(Long id);
    ResponseEntity<ApiResponse<Object>> getOne(Long id);
    ResponseEntity<ApiResponse<Object>> getListProductCategory(
            String search,
            int page,
            int size,
            String sort,
            String direction,
            HttpServletRequest request
    );
}


