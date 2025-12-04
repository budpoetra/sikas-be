package com.juaracoding.sikas.dto.validation;

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
import com.juaracoding.sikas.dto.validation.group.*;
import lombok.Data;

@Data
public class UserDTO {

    @NotNull(message = "TypeId is required", groups = {Create.class})
    private Integer typeId;

    @NotBlank(message = "Full name is required", groups = {Create.class, Update.class})
    @Size(max = 100, message = "Full name must be max 100 characters", groups = {Create.class, Update.class})
    private String fullName;

    @NotBlank(message = "Username is required", groups = {Create.class})
    @Size(max = 12, message = "Username must be max 12 characters", groups = {Create.class})
    private String username;

    @NotBlank(message = "Password is required", groups = {Create.class})
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@_#\\-$])[\\w].{8,15}$",
            message = "Invalid password format. Must contain uppercase, lowercase, number, and special character",
            groups = {Create.class}
    )
    private String password;

    @NotBlank(message = "Confirm password is required", groups = {Create.class})
    private String confirmPassword;

    @NotBlank(message = "Phone is required", groups = {Create.class, Update.class})
    @Size(max = 15, message = "Phone max length is 15", groups = {Create.class, Update.class})
    private String phone;

    @NotBlank(message = "Email is required", groups = {Create.class, Update.class})
    @Size(max = 100, message = "Email must be max 100 characters", groups = {Create.class, Update.class})
    @Email(message = "Invalid email format", groups = {Create.class, Update.class})
    private String email;

    private Byte status = 1;

}



