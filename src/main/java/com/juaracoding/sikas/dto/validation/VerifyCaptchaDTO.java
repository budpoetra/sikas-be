package com.juaracoding.sikas.dto.validation;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-253.28294.334, built on December 5, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/10/2025 1:16 AM
@Last Modified 12/10/2025 1:16 AM
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyCaptchaDTO {
    private String captchaToken;
}