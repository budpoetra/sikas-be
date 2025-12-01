package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 12/1/2025 21:38
@Last Modified 12/1/2025 21:38
Version 1.0
*/


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LowStockItemResponse {

    private Long id;
    private String productName;
    private Integer stock;
    private String status;

}



