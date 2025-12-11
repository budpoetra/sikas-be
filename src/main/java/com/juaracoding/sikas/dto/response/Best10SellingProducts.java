package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-253.28294.334, built on December 5, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/11/2025 12:44 PM
@Last Modified 12/11/2025 12:44 PM
Version 1.0
*/

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class Best10SellingProducts {
    private final Long id;
    private final String productName;
    private final Integer totalSold;
}