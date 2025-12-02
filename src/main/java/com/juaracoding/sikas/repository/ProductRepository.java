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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByProductCode(String productCode);

    @Query("SELECT SUM(p.stock) FROM Product p")
    Long getTotalStock();

    @Query("SELECT p FROM Product p WHERE p.stock <= :threshold ORDER BY p.stock ASC")
    List<Product> findLowStock(Integer threshold);
}



