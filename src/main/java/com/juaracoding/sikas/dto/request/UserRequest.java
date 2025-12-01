package com.juaracoding.sikas.dto.request;

import lombok.Data;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/25/2025 19:54
@Last Modified 11/25/2025 19:54
Version 1.0
*/

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequest {

    @NotNull(message = "TypeId is required")
    private Integer typeId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    @Size(max = 12, message = "Username must be max 12 characters")
    private String username;

    // Password TIDAK WAJIB saat update → tapi wajib saat create
    // Solusinya: validasi wajib password dilakukan di SERVICE saat create.
    private String password;

    @NotBlank(message = "Phone is required")
    @Size(max = 15, message = "Phone max length is 15")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Optional during update
    private Byte status;
}



