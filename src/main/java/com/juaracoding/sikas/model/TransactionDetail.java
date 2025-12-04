package com.juaracoding.sikas.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TransactionDetail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "TransactionId", nullable = false)
    private Integer transactionId;

    @Column(name = "ProductId", nullable = false)
    private Long productId;

    @Column(name = "QtyTransaction", nullable = false)
    private Integer qtyTransaction;

    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    @Column(name = "TotalPrice", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "CreatedDate", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "CreatedBy", nullable = false)
    private Integer createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TransactionId", insertable = false, updatable = false)
    private Transaction transaction;

    // Relationship dengan Product (jika ada entity Product)
     // @ManyToOne(fetch = FetchType.LAZY)
     // @JoinColumn(name = "ProductId", insertable = false, updatable = false)
     // private MasterProducts product;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
