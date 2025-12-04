package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:10
@Last Modified 11/23/2025 19:10
Version 1.0
*/


import com.juaracoding.sikas.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductCode(String productCode);

    @Query("SELECT SUM(p.stock) FROM Product p")
    Long getTotalStock();

    @Query("SELECT p FROM Product p WHERE p.stock <= :threshold ORDER BY p.stock ASC")
    List<Product> findLowStock(Integer threshold);

    @Query("""
           SELECT p 
           FROM Product p
           JOIN p.transactionDetails td
           JOIN td.transaction t
           WHERE t.createdDate BETWEEN :startDate AND :endDate
           GROUP BY p.id, p.productName, p.productCode, p.barcode, p.price, p.stock, p.status, p.categoryId, p.createdDate, p.updatedDate, p.createdBy, p.updatedBy
           ORDER BY SUM(td.qtyTransaction) DESC
           """)
    List<Product> findTop5Products(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
        SELECT p FROM Product p 
        WHERE (:search IS NULL 
                  OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :search, '%'))
                  OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :search, '%'))
            )
    """)
    Page<Product> searchListProduct(@Param("search") String search, Pageable pageable);
}



