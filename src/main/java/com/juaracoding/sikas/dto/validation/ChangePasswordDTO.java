package com.juaracoding.sikas.dto.validation;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/30/2025 4:46 PM
@Last Modified 11/30/2025 4:46 PM
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
public class ChangePasswordDTO {

    @NotBlank(message = "Old password must not be blank")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@_#\\-$])[A-Za-z\\d@_#\\-$]{8,15}$",
            message = "Old password must be 8-15 characters long, include uppercase, lowercase, digit, and a special character."
    )
    private String oldPassword;

    @NotBlank(message = "New password must not be blank")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@_#\\-$])[A-Za-z\\d@_#\\-$]{8,15}$",
            message = "New password must be 8-15 characters long, include uppercase, lowercase, digit, and a special character."
    )
    private String newPassword;

    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword;
}