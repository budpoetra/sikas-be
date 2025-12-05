package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/3/2025 12:06 PM
@Last Modified 12/3/2025 12:06 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TransactionDetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "TransactionId", nullable = false)
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "TransactionId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_TransactionDetails_Transactions")
    )
    @JsonBackReference
    private Transaction transaction;

    @Column(name = "ProductId", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ProductId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_TransactionDetails_Products")
    )
    @JsonBackReference
    private Product product;

    @Column(name = "QtyTransaction", nullable = false)
    private Integer qtyTransaction;

    @Column(name = "Price", nullable = false, precision = 13, scale = 0)
    private BigDecimal price;

    @Column(name = "TotalPrice", nullable = false, precision = 16, scale = 0)
    private BigDecimal totalPrice;

    @CreatedDate
    @Column(name = "CreatedDate", nullable = false)
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "CreatedBy", nullable = false)
    private Integer createdBy;
}