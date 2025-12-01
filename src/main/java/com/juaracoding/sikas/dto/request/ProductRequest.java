package com.juaracoding.sikas.dto.request;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:55
@Last Modified 11/23/2025 19:55
Version 1.0
*/

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotNull(message = "CategoryId is required")
    private Long categoryId;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Product code is required")
    private String productCode;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotBlank(message = "Barcode is required")
    private String barcode;

    @NotNull(message = " Stock is required")
    private Integer stock;

    private Integer status; // optional, default 1
}



