package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/14/2025 5:49 PM
@Last Modified 11/14/2025 5:49 PM
Version 1.0
*/

import com.juaracoding.sikas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("""
        SELECT DISTINCT u FROM User u
            JOIN FETCH u.masterUserType mut
            LEFT JOIN FETCH mut.permissions p
            LEFT JOIN FETCH p.masterFeature f
            LEFT JOIN FETCH p.masterAction a
            WHERE p.isAllowed = true
              AND u.username = :username
    """)
    Optional<User> fetchUserAndPermissionsByUsername(@Param("username") String username);
}