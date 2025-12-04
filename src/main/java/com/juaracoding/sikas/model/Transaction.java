package com.juaracoding.sikas.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UserId", nullable = false)
    private Integer userId;

    @Column(name = "TransactionNumber", nullable = false, unique = true)
    private String transactionNumber;

    @Column(name = "CreatedDate", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "TotalPriceTransaction", nullable = false, precision = 13, scale = 0)
    private BigDecimal totalPriceTransaction;

    // Relationship dengan User (jika ada entity User)
     // @ManyToOne(fetch = FetchType.LAZY)
     // @JoinColumn(name = "UserId", insertable = false, updatable = false)
     // private User user;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionDetail> transactionDetails;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}