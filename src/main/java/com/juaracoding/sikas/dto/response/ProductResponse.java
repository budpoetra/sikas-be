package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:56
@Last Modified 11/23/2025 19:56
Version 1.0
*/

import lombok.*;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String productName;
    private String productCode;
    private Long categoryId;
    private BigDecimal price;
    private String barcode;
    private Integer stock;
    private Integer status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer createdBy;
    private Integer updatedBy;

}


