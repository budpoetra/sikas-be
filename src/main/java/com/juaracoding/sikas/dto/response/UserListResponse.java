package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 6:21 PM
@Last Modified 12/4/2025 6:21 PM
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserListResponse {
    private Integer id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String typeName;
}