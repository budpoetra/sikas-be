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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "MasterProducts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_Product_ProductCode",
                        columnNames = {"ProductCode"}
                ),
                @UniqueConstraint(
                        name = "UK_Product_Barcode",
                        columnNames = {"Barcode"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "CategoryId", nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "CategoryId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_Products_ProductCategories")
    )
    private ProductCategory category;

    @Column(name = "ProductName", nullable = false)
    private String productName;

    @Column(name = "ProductCode", nullable = false, unique = true, length = 4)
    private String productCode;

    @Column(name = "Price", nullable = false, precision = 13, scale = 0)
    private BigDecimal price;

    @Column(name = "Barcode", nullable = false, unique = true, length = 13)
    private String barcode;

    @Builder.Default
    @Column(name = "Stock", nullable = false)
    private Integer stock = 0;

    @Builder.Default
    @Column(name = "Status", nullable = false)
    private Integer status = 0; // 1 = Active, 0 = Inactive

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TransactionDetail> transactionDetails;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProductEntry> productDeliveries;

    @Column(name = "CreatedDate", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

    @CreatedBy
    @Column(name = "CreatedBy")
    private Integer createdBy;

    @LastModifiedBy
    @Column(name = "UpdatedBy")
    private Integer updatedBy;

    @Transient
    public String getStatusName() {
        return switch (status) {
            case 1 -> "Active";
            case 0 -> "Inactive";
            default -> "Unknown";
        };
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}



