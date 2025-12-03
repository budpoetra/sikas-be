package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/3/2025 12:42 PM
@Last Modified 12/3/2025 12:42 PM
Version 1.0
*/

import com.juaracoding.sikas.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT SUM(t.totalPriceTransaction) FROM Transaction t WHERE t.createdDate BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalPriceTransactionByCreatedDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Transaction> findTop10ByOrderByCreatedDateDesc();
}