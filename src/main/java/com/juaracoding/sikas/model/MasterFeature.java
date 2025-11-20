package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/13/2025 12:36 PM
@Last Modified 11/13/2025 12:36 PM
Version 1.0
*/

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "MasterFeature",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_FeatureName", columnNames = "Name")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "UpdatedDate", nullable = false)
    private LocalDateTime updatedDate;

    // === Lifecycle Hooks ===
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}