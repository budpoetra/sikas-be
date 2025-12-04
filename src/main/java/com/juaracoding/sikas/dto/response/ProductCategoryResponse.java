package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 11:53
@Last Modified 11/23/2025 11:53
Version 1.0
*/


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryResponse {
    private Long id;
    private String category;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer createdBy;
    private Integer updatedBy;
}



