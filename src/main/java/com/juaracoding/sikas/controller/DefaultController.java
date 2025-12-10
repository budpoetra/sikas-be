package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/16/2025 10:57 AM
@Last Modified 11/16/2025 10:57 AM
Version 1.0
*/

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DefaultController {

    @GetMapping("/")
    public ResponseEntity<Object> defaultRoute() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Welcome to SIKAS API v1.0");
        response.put("status", 200);
        response.put("data", null);

        return ResponseEntity.ok(response);

    }
}