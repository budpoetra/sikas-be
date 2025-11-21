package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/15/2025 11:33 AM
@Last Modified 11/15/2025 11:33 AM
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.LoginDTO;
import com.juaracoding.sikas.dto.response.AuthResponse;
import com.juaracoding.sikas.model.User;
import com.juaracoding.sikas.service.AuthService;
import com.juaracoding.sikas.util.DtoToModelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration = 86400000L; // Default 24 hours in milliseconds

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(
            @Valid @RequestBody LoginDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ResponseEntity<ApiResponse<Object>> serviceResponse = authService.login(
                DtoToModelUtil.map(dto, User.class),
                request
        );

        ApiResponse<Object> body = serviceResponse.getBody();

        if (body == null || body.getData() == null) {
            return serviceResponse;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) body.getData();

        String refreshToken = (String) map.get("refreshToken");
        AuthResponse authResponse = (AuthResponse) map.get("authResponse");

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth")
                .maxAge(refreshTokenExpiration / 1000)
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());

        ApiResponse<Object> finalResponse = new ApiResponse<>(
                body.isSuccess(),
                body.getMessage(),
                body.getStatus(),
                authResponse
        );

        return new ResponseEntity<>(finalResponse, serviceResponse.getStatusCode());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Object>> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ResponseEntity<ApiResponse<Object>> serviceResponse = authService.refreshToken(
                refreshToken,
                request
        );

        ApiResponse<Object> body = serviceResponse.getBody();

        if (body == null || body.getData() == null) {
            return serviceResponse;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) body.getData();

        String newRefreshToken = (String) map.get("newRefreshToken");
        AuthResponse authResponse = (AuthResponse) map.get("authResponse");

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth")
                .maxAge(refreshTokenExpiration / 1000)
                .build();

        response.addHeader("Set-Cookie", refreshCookie.toString());

        ApiResponse<Object> finalResponse = new ApiResponse<>(
                body.isSuccess(),
                body.getMessage(),
                body.getStatus(),
                authResponse
        );

        return new ResponseEntity<>(finalResponse, serviceResponse.getStatusCode());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken != null && !refreshToken.isEmpty()) {
            authService.logout(refreshToken);
        }

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", deleteCookie.toString());

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                true,
                "Logged out successfully",
                HttpServletResponse.SC_OK,
                null
        );

        return new ResponseEntity<>(apiResponse, org.springframework.http.HttpStatus.OK);
    }
}