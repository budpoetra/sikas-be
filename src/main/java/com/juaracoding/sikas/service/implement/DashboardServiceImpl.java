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

import com.juaracoding.sikas.dto.response.DashboardSummaryResponse;
import com.juaracoding.sikas.dto.response.LowStockItemResponse;
import com.juaracoding.sikas.model.Product;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;

    @Override
    public DashboardSummaryResponse getSummary() {

        Long totalProducts = productRepository.count();
        Long totalStock = productRepository.getTotalStock();
        if (totalStock == null) totalStock = 0L;

        var lowStock = productRepository.findLowStock(10);

        return DashboardSummaryResponse.builder()
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

    }
}


