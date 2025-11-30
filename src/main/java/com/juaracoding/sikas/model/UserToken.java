package com.juaracoding.sikas.model;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/13/2025 6:38 PM
@Last Modified 11/13/2025 6:38 PM
Version 1.0
*/

import jakarta.persistence.*;
import lombok.*;
import com.juaracoding.sikas.constant.TokenType;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserToken")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "UserId", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "UserId",
            referencedColumnName = "Id",
            insertable = false,
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_UserToken_Users")
    )
    private User user;

    @Column(name = "Token", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "TokenType", nullable = false, length = 10)
    private TokenType tokenType = TokenType.ACCESS;

    @Column(name = "Expired", nullable = false)
    private boolean expired = false;

    @Column(name = "Revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "CreatedDate", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "ExpiredDate", nullable = false)
    private LocalDateTime expiredDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @Transient
    public boolean isValid() {
        return !expired && !revoked && expiredDate.isAfter(LocalDateTime.now());
    }
}