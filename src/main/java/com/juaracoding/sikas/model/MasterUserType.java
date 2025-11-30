package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/11/2025 1:40 PM
@Last Modified 11/11/2025 1:40 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "MasterUserType",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_UserType", columnNames = "UserType")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterUserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "UserType", nullable = false, length = 50)
    private String userType;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(
            mappedBy = "masterUserType",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<UserTypePermission> permissions;

    @Column(name = "UpdatedDate", nullable = false)
    private LocalDateTime updatedDate;

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