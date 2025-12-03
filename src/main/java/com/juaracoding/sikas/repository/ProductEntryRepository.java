package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 12:41 AM
@Last Modified 12/4/2025 12:41 AM
Version 1.0
*/

import com.juaracoding.sikas.model.ProductEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductEntryRepository extends JpaRepository<ProductEntry, Long> {

    @Query(value = """
        SELECT TOP 10 pe.*, p.ProductName, u.FullName
        FROM ProductEntry pe
        JOIN MasterProducts p ON pe.ProductId = p.Id
        JOIN Users u ON pe.CreatedBy = u.Id
        WHERE pe.CreatedDate BETWEEN :startDate AND :endDate
        ORDER BY pe.CreatedDate DESC
    """, nativeQuery = true)
    List<Object[]> findTop10ProductEntries(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}
