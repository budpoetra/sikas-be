package com.juaracoding.sikas.exception;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/16/2025 1:16 PM
@Last Modified 11/16/2025 1:16 PM
Version 1.0
*/

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResponseHandler {

    public static ResponseEntity<Object> handleResponse(
            HttpStatus status,
            String message,
            Object data
    ){

        Map<String, Object> m = new HashMap<>();
        m.put("status", status.value());
        m.put("success", !status.isError());
        m.put("message", message);
        m.put("timestamp", Instant.now().toString());
        m.put("data", data);

        return new ResponseEntity<>(m,status);
    }
}