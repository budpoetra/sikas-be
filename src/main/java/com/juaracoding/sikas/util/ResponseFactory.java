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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.juaracoding.sikas.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class ResponseFactory {

    private static final ObjectMapper mapper = new ObjectMapper();

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

    public static void errorFilter(
            String message,
            int status,
            HttpServletResponse response
    ) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        ObjectNode json = mapper.createObjectNode();
        json.put("success", false);
        json.put("status", status);
        json.put("message", message);
        json.putNull("data");

        response.getWriter().write(mapper.writeValueAsString(json));
    }
}