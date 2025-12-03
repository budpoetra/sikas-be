package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/13/2025 12:47 PM
@Last Modified 11/13/2025 12:47 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserTypePermission")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTypePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "UserTypeId", nullable = false)
    private Integer userTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "UserTypeId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_UserTypePermission_MasterUserType")
    )
    @JsonBackReference
    private MasterUserType masterUserType;

    @Column(name = "FeatureId", nullable = false)
    private Integer featureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "FeatureId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_UserTypePermission_MasterFeature")
    )
    private MasterFeature masterFeature;

    @Column(name = "ActionId", nullable = false)
    private Integer actionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ActionId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_UserTypePermission_MasterAction")
    )
    private MasterAction masterAction;

    @Column(name = "IsAllowed", nullable = false)
    private Boolean isAllowed;

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