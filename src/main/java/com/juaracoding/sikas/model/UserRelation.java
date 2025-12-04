package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/13/2025 4:16 PM
@Last Modified 11/13/2025 4:16 PM
Version 1.0
*/

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "UserRelation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @CreatedBy
    @Column(name = "OwnerId", nullable = false)
    private Integer ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "OwnerId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_UserRelation_Users_Owner")
    )
    @JsonBackReference
    private User owner;

    @Column(name = "UserId", nullable = false)
    private Integer userId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(
            name = "UserId",
            referencedColumnName = "Id",
            nullable = false,
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_UserRelation_Users_User")
    )
    private User user;

    @CreatedBy
    @Column(name = "CreatedBy", nullable = false)
    private Integer createdBy;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private java.time.LocalDateTime createdDate;

    @Column(name = "UpdatedDate", nullable = false)
    private java.time.LocalDateTime updatedDate;

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