package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 12/1/2025 21:44
@Last Modified 12/1/2025 21:44
Version 1.0
*/

import com.juaracoding.sikas.dto.response.DashboardSummaryResponse;
import com.juaracoding.sikas.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<?> getDashboardSummary() {
        DashboardSummaryResponse summary = dashboardService.getSummary();
        return ResponseEntity.ok(summary);
    }
}


