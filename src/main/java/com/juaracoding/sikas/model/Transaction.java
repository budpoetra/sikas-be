package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/3/2025 12:00 PM
@Last Modified 12/3/2025 12:00 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "Transactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_Transaction_TransactionNumber",
                        columnNames = {"TransactionNumber"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @CreatedBy
    @Column(name = "UserId", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "UserId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_Transaction_User")
    )
    @JsonBackReference
    private User user;

    @Column(name = "TransactionNumber", nullable = false, length = 100)
    private String transactionNumber;

    @OneToMany(
            mappedBy = "transaction",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<TransactionDetail> transactionDetails;

    @Column(name = "TotalPriceTransaction", nullable = false, precision = 16, scale = 0)
    private BigDecimal totalPriceTransaction;

    @CreatedDate
    @Column(name = "CreatedDate", nullable = false)
    private LocalDateTime createdDate;
}