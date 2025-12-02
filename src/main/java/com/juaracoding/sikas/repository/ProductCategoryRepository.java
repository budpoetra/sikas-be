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
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    boolean existsByCategory(String category);
}

