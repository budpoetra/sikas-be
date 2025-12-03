package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/3/2025 9:41 PM
@Last Modified 12/3/2025 9:41 PM
Version 1.0
*/

import com.juaracoding.sikas.model.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {
}
