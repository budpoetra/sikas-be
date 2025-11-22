package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/15/2025 11:26 AM
@Last Modified 11/15/2025 11:26 AM
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

/**
 * Platform Code: AUT
 * Module Code: 001
 */
public interface AuthService {
    ResponseEntity<ApiResponse<Object>> login(User user, HttpServletRequest request);
    ResponseEntity<ApiResponse<Object>> refreshToken(String refreshToken, HttpServletRequest request);
    void logout(String refreshToken);
}