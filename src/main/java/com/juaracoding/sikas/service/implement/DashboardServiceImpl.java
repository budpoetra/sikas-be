package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 12/1/2025 21:44
@Last Modified 12/1/2025 21:44
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.DashboardSummaryResponse;
import com.juaracoding.sikas.dto.response.LowStockItemResponse;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.service.DashboardService;
import com.juaracoding.sikas.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;

    /**
     * Get dashboard summary including total products, total stock,
     * Platform Code: DSB
     * Module Code: 006
     * Quota Code: 01 - 10
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> getSummary() {

        try {
            Long totalProducts = productRepository.count();
            Long totalStock = productRepository.getTotalStock();
            if (totalStock == null) totalStock = 0L;

            var lowStock = productRepository.findLowStock(10);

            DashboardSummaryResponse response = DashboardSummaryResponse.builder()
                    .totalProducts(totalProducts)
                    .totalStock(totalStock)
                    .todayTransactions(0L) // BELUM ADA TRANSAKSI
                    .lowStockItems(
                            lowStock.stream()
                                    .map(p -> LowStockItemResponse.builder()
                                            .id(p.getId())
                                            .productName(p.getProductName())
                                            .stock(p.getStock())
                                            .status(p.getStock() <= 5 ? "Low Stock" : "Warning")
                                            .build()
                                    )
                                    .collect(Collectors.toList())
                    )
                    .build();

            return ResponseFactory.success(
                    "Dashboard summary retrieved successfully",
                    HttpStatus.OK,
                    response
            );
        } catch (Exception e) {
            log.warn("DSB006E01 - Error retrieving dashboard summary: {}", e.getMessage());

            return ResponseFactory.error(
                    "DSB006E01 - Failed to retrieve dashboard summary",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }

    }
}


