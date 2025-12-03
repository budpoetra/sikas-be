package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/2/2025 4:36 PM
@Last Modified 12/2/2025 4:36 PM
Version 1.0
*/

import com.juaracoding.sikas.annotation.Loggable;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Loggable
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getReport(@RequestParam(required = false) String startDate,
                                                         @RequestParam(required = false) String endDate) {

        return reportService.getReport(startDate, endDate);

    }

    @Loggable
    @GetMapping("/transaction-list")
    public ResponseEntity<ApiResponse<Object>> getReportList() {

        return reportService.getReportTransactionList();

    }

    @Loggable
    @GetMapping("/product-entry")
    public ResponseEntity<ApiResponse<Object>> getReportProductEntry(@RequestParam(required = false) String startDate,
                                                                     @RequestParam(required = false) String endDate) {

        return reportService.getReportProductEntry(startDate, endDate);

    }

}