package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/15/2025 4:29 PM
@Last Modified 11/15/2025 4:29 PM
Version 1.0
*/

import com.juaracoding.sikas.constant.TokenType;
import com.juaracoding.sikas.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {
    boolean existsByTokenAndExpiredFalseAndRevokedFalse(String token);

    Optional<UserToken> findByTokenAndTokenType(String token, TokenType tokenType);

    Optional<UserToken> findByToken(String token);

    List<UserToken> findAllByUserIdAndTokenTypeAndRevokedFalse(Integer userId, TokenType tokenType);
}