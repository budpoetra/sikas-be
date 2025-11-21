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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenRepository userTokenRepository;

    @Override
    public boolean isTokenSafe(String token) {
        return userTokenRepository.existsByTokenAndExpiredFalseAndRevokedFalse(token);
    }

    @Override
    public java.util.Optional<com.juaracoding.sikas.model.UserToken> findByToken(String token) {
        return userTokenRepository.findByToken(token);
    }

    @Override
    public void save(com.juaracoding.sikas.model.UserToken userToken) {
        userTokenRepository.save(userToken);
    }

}