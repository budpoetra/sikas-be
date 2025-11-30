package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/15/2025 11:31 AM
@Last Modified 11/15/2025 11:31 AM
Version 1.0
*/

import com.juaracoding.sikas.constant.TokenType;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.AuthResponse;
import com.juaracoding.sikas.model.User;
import com.juaracoding.sikas.model.UserToken;
import com.juaracoding.sikas.repository.UserRepository;
import com.juaracoding.sikas.repository.UserTokenRepository;
import com.juaracoding.sikas.security.UserDetailsImpl;
import com.juaracoding.sikas.service.AuthService;
import com.juaracoding.sikas.security.JwtUtil;

import com.juaracoding.sikas.util.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    /**
     * Login user and generate JWT tokens
     * Platform Code: AUT
     * Module Code: 001
     * Quota Code: 01 - 10
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> login(User user, HttpServletRequest request) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );

            String username = auth.getName();
            String accessToken = jwtUtil.generateToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);

            // Get token expiration dates
            LocalDateTime accessExpiredAt = jwtUtil.getAccessTokenExpiration(accessToken);
            LocalDateTime refreshExpiredAt = jwtUtil.getRefreshTokenExpiration(refreshToken);

            Set<String> roles = auth.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            User loggedInUser = ((UserDetailsImpl) auth.getPrincipal()).getUser();

            // Save access token and refresh token to database
            List<UserToken> tokensToSave = List.of(
                    UserToken.builder()
                            .userId(loggedInUser.getId())
                            .token(accessToken)
                            .tokenType(TokenType.ACCESS)
                            .expiredDate(accessExpiredAt)
                            .expired(false)
                            .revoked(false)
                            .build(),
                    UserToken.builder()
                            .userId(loggedInUser.getId())
                            .token(refreshToken)
                            .tokenType(TokenType.REFRESH)
                            .expiredDate(refreshExpiredAt)
                            .expired(false)
                            .revoked(false)
                            .build()
            );
            userTokenRepository.saveAll(tokensToSave);

            AuthResponse authResponse = new AuthResponse(
                    accessToken,
                    "Bearer",
                    accessExpiredAt,
                    username,
                    roles
            );

            return ResponseFactory.success(
                    "Login successful",
                    HttpStatus.OK,
                    Map.of(
                            "authResponse", authResponse,
                            "refreshToken", refreshToken
                    )
            );

        } catch (BadCredentialsException ex) {
            log.error("AUT001E01 - Invalid username or password: {}", ex.getMessage(), ex);

            return ResponseFactory.error(
                    "AUT001E01 - Invalid username or password",
                    HttpStatus.UNAUTHORIZED,
                    null
            );

        } catch (DisabledException ex) {
            log.error("AUT001E02 - User account is disabled: {}", ex.getMessage(), ex);

            return ResponseFactory.error(
                    "AUT001E02 - User account is disabled",
                    HttpStatus.FORBIDDEN,
                    null
            );

        } catch (LockedException ex) {
            log.error("AUT001E03 - User account is locked: {}", ex.getMessage(), ex);

            return ResponseFactory.error(
                    "AUT001E03 - User account is locked",
                    HttpStatus.FORBIDDEN,
                    null
            );

        } catch (Exception ex) {
            log.error("AUT001E10 - An unexpected error occurred during login: {}", ex.getMessage(), ex);

            return ResponseFactory.error(
                    "AUT001E10 - An unexpected error occurred during login",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    /**
     * Refresh JWT tokens using refresh token
     * Platform Code: AUT
     * Module Code: 001
     * Quota Code: 11 - 20
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> refreshToken(String refreshToken, HttpServletRequest request) {
        try {
            // Get existing token from database
            UserToken oldToken = userTokenRepository.findByTokenAndTokenType(refreshToken, TokenType.REFRESH)
                    .orElseThrow(() -> {
                        log.warn("AUT001W11 - Refresh token not found in database");

                        return new Exception("AUT001W11 - Refresh token not found");
                    });

            if (!jwtUtil.validateRefreshToken(refreshToken)) {
                log.warn("AUT001W12 - Invalid or expired refresh token");

                oldToken.setExpired(true);
                userTokenRepository.save(oldToken);

                return ResponseFactory.error(
                        "Auth001W12 - Invalid or expired refresh token",
                        HttpStatus.UNAUTHORIZED,
                        null
                );
            }

            String username = jwtUtil.getUsernameFromRefreshToken(oldToken.getToken());

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.warn("AUT001W13 - User not found for username: {}", username);

                        return new Exception("AUT001W13 - User not found");
                    });

            Integer userId = user.getId();

            // Generate new access token
            String newAccessToken = jwtUtil.generateToken(username);
            LocalDateTime accessExpiredAt = jwtUtil.getAccessTokenExpiration(newAccessToken);

            // Generate new refresh token
            String newRefreshToken = jwtUtil.generateRefreshToken(username);
            LocalDateTime refreshExpiredAt = jwtUtil.getRefreshTokenExpiration(newRefreshToken);

            // Save new refresh token to database
            UserToken newUserToken = UserToken.builder()
                    .userId(userId)
                    .token(newRefreshToken)
                    .tokenType(TokenType.REFRESH)
                    .expiredDate(refreshExpiredAt)
                    .expired(false)
                    .revoked(false)
                    .build();
            userTokenRepository.save(newUserToken);

            // Set old refresh token as revoked
            oldToken.setRevoked(true);
            userTokenRepository.save(oldToken);

            // Get user roles
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getMasterUserType().getUserType()));

            Set<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());


            AuthResponse authResponse = new AuthResponse(
                    newAccessToken,
                    "Bearer",
                    accessExpiredAt,
                    username,
                    roles
            );

            return ResponseFactory.success(
                    "Token refreshed successfully",
                    HttpStatus.OK,
                    Map.of(
                            "authResponse", authResponse,
                            "newRefreshToken", newRefreshToken
                    )
            );

        } catch (Exception ex) {
            log.error("AUT001E20 - An error occurred while refreshing token: {}", ex.getMessage(), ex);

            return ResponseFactory.error(
                    "AUT001E20 - An error occurred while refreshing token",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    /**
     * Logout user by revoking the refresh token
     * Platform Code: AUT
     * Module Code: 001
     * Quota Code: 21 - 30
     */
    @Override
    public void logout(String refreshToken) {
        try {
            userTokenRepository.findByTokenAndTokenType(refreshToken, TokenType.REFRESH)
                    .ifPresent(oldToken -> {
                        oldToken.setRevoked(true);
                        userTokenRepository.save(oldToken);
                    });

            // Revoke all access tokens associated with the user of the refresh token
            userTokenRepository.findByTokenAndTokenType(refreshToken, TokenType.REFRESH)
                    .ifPresent(oldRefreshToken -> {
                        Integer userId = oldRefreshToken.getUserId();
                        List<UserToken> accessTokens = userTokenRepository.findAllByUserIdAndTokenTypeAndRevokedFalse(userId, TokenType.ACCESS);
                        for (UserToken accessToken : accessTokens) {
                            accessToken.setRevoked(true);
                        }
                        userTokenRepository.saveAll(accessTokens);
                    });
        } catch (Exception ex) {
            log.error("AUT001E30 - An error occurred during logout: {}", ex.getMessage(), ex);
        }

    }
}
