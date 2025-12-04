package com.juaracoding.sikas.dto.validation;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:55
@Last Modified 11/23/2025 19:55
Version 1.0
*/

import com.juaracoding.sikas.dto.validation.group.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    @NotNull(message = "CategoryId is required", groups = {Create.class, Update.class})
    private Long categoryId;

    @NotBlank(message = "Product name is required", groups = {Create.class, Update.class})
    private String productName;

    @NotBlank(message = "Product code is required", groups = {Create.class})
    @Size(max = 4, message = "Product code must not exceed 4 characters", groups = {Create.class})
    private String productCode;

    @NotNull(message = "Price is required", groups = {Create.class, Update.class})
    @Digits(integer = 13, fraction = 0,
            message = "Price must be a valid number with up to 13 digits and 0 decimals",
            groups = {Create.class, Update.class})
    private BigDecimal price;

    private Integer status = 1;

}



