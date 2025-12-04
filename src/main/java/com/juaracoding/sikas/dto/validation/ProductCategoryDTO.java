package com.juaracoding.sikas.dto.validation;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 11:53
@Last Modified 11/23/2025 11:53
Version 1.0
*/


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductCategoryDTO {

    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 50, message = "Category must be between 3 and 50 characters")
    private String category;

}



