package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 11:51
@Last Modified 11/23/2025 11:51
Version 1.0
*/

import com.juaracoding.sikas.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    boolean existsByCategory(String category);

    @Query(value = """
       SELECT TOP 5 pc.*
       FROM MasterProductCategories pc
       JOIN MasterProducts p ON pc.Id = p.CategoryId
       JOIN TransactionDetails td ON p.Id = td.ProductId
       JOIN Transactions t ON t.Id = td.TransactionId
       WHERE t.CreatedDate BETWEEN :startDate AND :endDate
       GROUP BY pc.Id, pc.Category, pc.CreatedDate, pc.UpdatedDate, pc.CreatedBy, pc.UpdatedBy
       ORDER BY SUM(td.QtyTransaction) DESC
       """, nativeQuery = true)
    List<ProductCategory> findTop5Categories(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
         SELECT pc
         FROM ProductCategory pc
         WHERE (:search IS NULL OR LOWER(pc.category) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<ProductCategory> searchListProductCategory(
            @Param("search") String search,
            Pageable pageable
    );
}

