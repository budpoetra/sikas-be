package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/2/2025 4:56 PM
@Last Modified 12/2/2025 4:56 PM
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ReportService {

    ResponseEntity<ApiResponse<Object>> getReport(String startDate, String endDate);

    ResponseEntity<ApiResponse<Object>> getReportTransactionList();

    ResponseEntity<ApiResponse<Object>> getReportProductEntry(String startDate, String endDate);

}
