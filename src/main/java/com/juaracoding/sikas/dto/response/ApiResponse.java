package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/18/2025 6:05 PM
@Last Modified 11/18/2025 6:05 PM
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private int status;
    private Instant timestamp;
    private T data;

    public ApiResponse(boolean success, String message, int status, T data) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.timestamp = Instant.now();
        this.data = data;
    }
}