package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:10
@Last Modified 11/23/2025 19:10
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.ProductDTO;
import com.juaracoding.sikas.dto.response.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Platform Code: PDT
 * Module Code: 004
 */
public interface ProductService {

    ResponseEntity<ApiResponse<Object>> create(ProductDTO request);

    ResponseEntity<ApiResponse<Object>> update(Long id, ProductDTO request);

    void delete(Long id);

    ResponseEntity<ApiResponse<Object>> getOne(Long id);

    List<ProductResponse> getAll();
}



