package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/11/2025 1:31 PM
@Last Modified 11/11/2025 1:31 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_Username", columnNames = "Username"),
                @UniqueConstraint(name = "UQ_Email", columnNames = "Email"),
                @UniqueConstraint(name = "UQ_Phone", columnNames = "Phone")
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "TypeId", nullable = false)
    private Integer typeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "TypeId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_Users_MasterUserType")
    )
    private MasterUserType masterUserType;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<UserRelation> userRelations;

    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "Username", nullable = false, length = 12)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "Status", nullable = false)
    private Byte status; // 0 = Inactive, 1 = Active

    @Transient
    public String getStatusName() {
        return switch (status) {
            case 0 -> "Inactive";
            case 1 -> "Active";
            default -> "Unknown";
        };
    }

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Transaction> transactions;

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