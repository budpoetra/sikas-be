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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

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

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsByPhoneAndIdNot(String phone, Integer id);

    @Query("""
        SELECT u FROM User u
        JOIN UserRelation ur ON u.id = ur.userId
        WHERE ur.ownerId = :id
          AND (
                :search IS NULL
                OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :search, '%'))
          )
    """)
    Page<User> searchListUsers(@Param("id") Integer id,
                               @Param("search") String search,
                               Pageable pageable);
}