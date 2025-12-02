package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:09
@Last Modified 11/23/2025 19:09
Version 1.0
*/

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "MasterProducts")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId", nullable = false)
    private ProductCategory category;

    @Column(name = "ProductName", nullable = false)
    private String productName;

    @Column(name = "ProductCode", nullable = false, unique = true)
    private String productCode;

    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    @Column(name = "Barcode", nullable = false)
    private String barcode;

    @Column(name = "Stock", nullable = false)
    private Integer stock;   // <── tambahan

    @Column(name = "Status", nullable = false)
    private Integer status;

    @Column(name = "CreatedDate", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

    @Column(name = "CreatedBy")
    private Long createdBy;

    @Column(name = "UpdatedBy")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        if (this.status == null) this.status = 1;
        if (this.stock == null) this.stock = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}



