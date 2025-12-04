package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 11:51
@Last Modified 11/23/2025 11:51
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "MasterProductCategories")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Category", nullable = false, unique = true)
    private String category;

    @CreatedDate
    @Column(name = "CreatedDate", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "UpdatedDate")
    private LocalDateTime updatedDate;

    @CreatedBy
    @Column(name = "CreatedBy")
    private Integer createdBy;

    @LastModifiedBy
    @Column(name = "UpdatedBy")
    private Integer updatedBy;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
}


