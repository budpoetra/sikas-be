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

@Controller
public class FallbackController {

    @RequestMapping("/**")
    public ResponseEntity<String> fallback() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error 404: Path not found");
    }
}