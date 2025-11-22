package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/21/2025 10:19 AM
@Last Modified 11/21/2025 10:19 AM
Version 1.0
*/

import com.juaracoding.sikas.model.UserToken;

import java.util.Optional;

/**
 * Platform Code: UST
 * Module Code: 002
 */
public interface UserTokenService {
    boolean isTokenSafe(String token);
    void save(UserToken userToken);
    Optional<UserToken> findByToken(String token);
}
