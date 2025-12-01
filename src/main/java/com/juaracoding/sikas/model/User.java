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
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_UserName", columnNames = "UserName"),
                @UniqueConstraint(name = "UQ_Email", columnNames = "Email"),
                @UniqueConstraint(name = "UQ_Phone", columnNames = "Phone")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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