package com.juaracoding.sikas.dto.validation;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/14/2025 5:50 PM
@Last Modified 11/14/2025 5:50 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Username cannot be blank")
    @Pattern(
            regexp = "^[\\w\\.]{6,12}$",
            message = "Invalid username format. Example: user.name123"
    )
    private String username;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@_#\\-$])[\\w].{8,15}$",
            message = "Invalid password format. Must contain uppercase, lowercase, number, and special character"
    )
    private String password;

}