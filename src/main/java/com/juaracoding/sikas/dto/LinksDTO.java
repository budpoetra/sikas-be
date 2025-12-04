package com.juaracoding.sikas.dto;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 6:55 PM
@Last Modified 12/4/2025 6:55 PM
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinksDTO {
    private String self;
    private String next;
    private String prev;
}
