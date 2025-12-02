package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/16/2025 11:00 AM
@Last Modified 11/16/2025 11:00 AM
Version 1.0
*/

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GlobalFallbackController {

    @RequestMapping(value = "/**", method = {
            RequestMethod.GET,
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH,
            RequestMethod.OPTIONS
    })
    public ResponseEntity<Object> fallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "The requested resource was not found.");
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("data", null);

        return ResponseEntity.ok(response);
    }
}