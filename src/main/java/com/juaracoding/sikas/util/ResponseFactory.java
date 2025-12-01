package com.juaracoding.sikas.util;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/18/2025 6:10 PM
@Last Modified 11/18/2025 6:10 PM
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory {

    public static <T> ResponseEntity<ApiResponse<T>> success(
            String message, HttpStatus status, T data) {

        ApiResponse<T> response = new ApiResponse<>(
                true,
                message,
                status.value(),
                data
        );

        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ApiResponse<Object>> error(
            String message, HttpStatus status, Object data) {

        ApiResponse<Object> response = new ApiResponse<>(
                false,
                message,
                status.value(),
                data
        );

        return new ResponseEntity<>(response, status);
    }
}