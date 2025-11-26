package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/21/2025 10:21 AM
@Last Modified 11/21/2025 10:21 AM
Version 1.0
*/

import com.juaracoding.sikas.repository.UserTokenRepository;
import com.juaracoding.sikas.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenRepository userTokenRepository;

    /**
     * Refresh JWT tokens using refresh token
     * Platform Code: UST
     * Module Code: 002
     * Quota Code: 01 - 10
     */
    @Override
    public boolean isTokenSafe(String token) {
        try {
            return userTokenRepository.existsByTokenAndExpiredFalseAndRevokedFalse(token);
        } catch (Exception e) {
            log.error("UST002E01 - Error checking token safety: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Find UserToken by token
     * Platform Code: UST
     * Module Code: 002
     * Quota Code: 11 - 20
     */
    @Override
    public Optional<com.juaracoding.sikas.model.UserToken> findByToken(String token) {
        try {
            return userTokenRepository.findByToken(token);
        } catch (Exception e) {
            log.error("UST002E11 - Error finding token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Save UserToken
     * Platform Code: UST
     * Module Code: 002
     * Quota Code: 21 - 30
     */
    @Override
    public void save(com.juaracoding.sikas.model.UserToken userToken) {
        try {
            userTokenRepository.save(userToken);
        } catch (Exception e) {
            log.error("UST002E21 - Error saving token: {}", e.getMessage());
        }
    }

}