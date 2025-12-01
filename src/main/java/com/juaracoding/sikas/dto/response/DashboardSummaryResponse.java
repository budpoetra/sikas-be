package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 12/1/2025 21:37
@Last Modified 12/1/2025 21:37
Version 1.0
*/

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardSummaryResponse {

    private Long totalProducts;
    private Long totalStock;
    private Long todayTransactions;
    private List<LowStockItemResponse> lowStockItems;

}



