package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/27/2025 3:53 PM
@Last Modified 11/27/2025 3:53 PM
Version 1.0
*/

import com.juaracoding.sikas.annotation.Loggable;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.ChangePasswordDTO;
import com.juaracoding.sikas.security.UserDetailsImpl;
import com.juaracoding.sikas.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @Loggable
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getProfile(userDetails.getUsername());
    }

    @Loggable
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@Valid @RequestBody ChangePasswordDTO dto,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              HttpServletRequest request) {

        return userService.changePassword(dto, userDetails.getUsername(), request);

    }
}