package com.juaracoding.sikas.model;

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