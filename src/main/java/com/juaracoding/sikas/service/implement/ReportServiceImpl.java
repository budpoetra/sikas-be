package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/2/2025 4:58 PM
@Last Modified 12/2/2025 4:58 PM
Version 1.0
*/

import com.juaracoding.sikas.model.*;
import com.juaracoding.sikas.repository.ProductCategoryRepository;
import com.juaracoding.sikas.repository.ProductEntryRepository;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.repository.TransactionRepository;
import com.juaracoding.sikas.service.ReportService;
import com.juaracoding.sikas.util.ResponseFactory;
import com.juaracoding.sikas.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final TransactionRepository transactionRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final ProductEntryRepository productEntryRepository;

    @Override
    public ResponseEntity<ApiResponse<Object>> getReport(String startDate, String endDate) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDateReport = startDate != null ? parseStringToDateTime(startDate) : now.withDayOfMonth(1);
            LocalDateTime endDateReport = endDate != null ? parseStringToDateTime(endDate) : now;

            Map<String, Object> response = new HashMap<>();
            response.put("summary", generateSummary(startDateReport, endDateReport));
            response.put("topCategories", generateTopCategories(startDateReport, endDateReport));
            response.put("topProducts", generateTopProducts(startDateReport, endDateReport));

            return ResponseFactory.success("Report generated successfully.", HttpStatus.OK, response);

        } catch (IllegalArgumentException e) {
            log.error("Invalid date format: {}", e.getMessage());
            return ResponseFactory.error(
                    "Invalid date format. Required format: yyyy-MM-dd HH:mm:ss",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        } catch (Exception e) {
            log.error("Error occurred while generating report: {}", e.getMessage());
            return ResponseFactory.error(
                    "An error occurred while generating the report.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    private Map<String, Object> generateSummary(LocalDateTime start, LocalDateTime end) {
        Map<String, Object> summary = new HashMap<>();
        try {
            BigDecimal totalTransactions = transactionRepository
                    .sumTotalPriceTransactionByCreatedDateBetween(start, end);
            summary.put("date", formatDateRange(start, end));
            summary.put("total", totalTransactions != null ? totalTransactions : BigDecimal.ZERO);
        } catch (Exception e) {
            log.error("Error calculating total transactions: {}", e.getMessage());
            summary.put("date", formatDateRange(start, end));
            summary.put("total", BigDecimal.ZERO);
        }
        return summary;
    }

    private List<Map<String, Object>> generateTopCategories(LocalDateTime start, LocalDateTime end) {
        try {
            List<ProductCategory> categories = productCategoryRepository.findTop5Categories(start, end);
            List<Map<String, Object>> result = new ArrayList<>();

            for (ProductCategory category : categories) {
                long totalQty = category.getProducts()
                        .stream()
                        .flatMap(p -> p.getTransactionDetails().stream())
                        .mapToLong(TransactionDetail::getQtyTransaction)
                        .sum();

                BigDecimal totalPrice = category.getProducts()
                        .stream()
                        .flatMap(p -> p.getTransactionDetails().stream())
                        .map(td -> td.getPrice().multiply(BigDecimal.valueOf(td.getQtyTransaction())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<String, Object> map = new HashMap<>();
                map.put("id", category.getId());
                map.put("name", category.getCategory());
                map.put("total", totalQty);
                map.put("totalPrice", totalPrice);

                result.add(map);
            }

            return result;

        } catch (Exception e) {
            log.error("Error fetching top categories: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<Map<String, Object>> generateTopProducts(LocalDateTime start, LocalDateTime end) {
        try {
            List<Product> products = productRepository.findTop5Products(start, end);
            List<Map<String, Object>> result = new ArrayList<>();

            for (Product product : products) {
                long totalQty = product.getTransactionDetails()
                        .stream()
                        .mapToLong(TransactionDetail::getQtyTransaction)
                        .sum();

                BigDecimal totalPrice = product.getTransactionDetails()
                        .stream()
                        .map(td -> td.getPrice().multiply(BigDecimal.valueOf(td.getQtyTransaction())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<String, Object> map = new HashMap<>();
                map.put("id", product.getId());
                map.put("name", product.getProductName());
                map.put("total", totalQty);
                map.put("totalPrice", totalPrice);

                result.add(map);
            }

            return result;

        } catch (Exception e) {
            log.error("Error fetching top products: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private LocalDateTime parseStringToDateTime(String date) {
        if (date == null) return null;
        try {
            return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Required format: yyyy-MM-dd HH:mm:ss");
        }
    }

    private String formatDateRange(LocalDateTime start, LocalDateTime end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
        return start.format(formatter) + " - " + end.format(formatter);
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getReportTransactionList() {
        try {
            List<Transaction> transaction = transactionRepository.findTop10ByOrderByCreatedDateDesc();
            List<Map<String, Object>> result = new ArrayList<>();

            for (Transaction tx : transaction) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", tx.getId());
                map.put("cashierName", tx.getUser().getFullName());
                map.put("transactionNumber", tx.getTransactionNumber());
                map.put("totalPriceTransaction", tx.getTotalPriceTransaction());
                map.put("createdDate", tx.getCreatedDate());
                result.add(map);
            }

            return ResponseFactory.success(
                    "Transaction list retrieved successfully.",
                    HttpStatus.OK,
                    result
            );

        } catch (Exception e) {
            log.error("Error retrieving transaction list: {}", e.getMessage());
            return ResponseFactory.error(
                    "An error occurred while retrieving the transaction list.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getReportProductEntry(String startDate, String endDate) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDateReport = startDate != null ? parseStringToDateTime(startDate) : now.withDayOfMonth(1);
            LocalDateTime endDateReport = endDate != null ? parseStringToDateTime(endDate) : now;

            List<Object[]> productEntries = productEntryRepository.findTop10ProductEntries(startDateReport, endDateReport);

            List<Map<String, Object>> result = new ArrayList<>();
            for (Object[] row : productEntries) {
                Map<String,Object> map = new HashMap<>();
                map.put("id", row[0]);
                map.put("productId", row[1]);
                map.put("qty", row[2]);
                map.put("createdDate", row[3]);
                map.put("createdBy", row[4]);
                map.put("productName", row[5]);
                map.put("createdName", row[6]);
                result.add(map);
            }

            return ResponseFactory.success(
                    "Product entry report generated successfully.",
                    HttpStatus.OK,
                    result
            );

        } catch (IllegalArgumentException e) {
            log.error("Invalid date format: {}", e.getMessage());
            return ResponseFactory.error(
                    "Invalid date format. Required format: yyyy-MM-dd HH:mm:ss",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        } catch (Exception e) {
            log.error("Error occurred while generating product entry report: {}", e.getMessage());
            return ResponseFactory.error(
                    "An error occurred while generating the product entry report.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }
}
