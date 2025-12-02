package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 12/1/2025 20:59
@Last Modified 12/1/2025 20:59
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Integer id;
    private Integer typeId;
    private String fullName;
    private String username;
    private String phone;
    private String email;
    private Byte status;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // ⬇ FIELD INI WAJIB OBJECT, BUKAN STRING
    private MasterUserTypeResponse userType;
}


