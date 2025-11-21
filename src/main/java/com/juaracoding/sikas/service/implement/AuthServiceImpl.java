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
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           UserTokenRepository userTokenRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userTokenRepository = userTokenRepository;
        this.userRepository = userRepository;
    }

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

            // Save tokens
            List<UserToken> tokens = List.of(
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

            userTokenRepository.saveAll(tokens);

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
            return ResponseFactory.error(
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED,
                    null
            );

        } catch (DisabledException ex) {
            return ResponseFactory.error(
                    "User account is disabled",
                    HttpStatus.FORBIDDEN,
                    null
            );

        } catch (LockedException ex) {
            return ResponseFactory.error(
                    "User account is locked",
                    HttpStatus.FORBIDDEN,
                    null
            );

        } catch (Exception ex) {
            log.error("Login failed: {}", ex.getMessage());
            return ResponseFactory.error(
                    "An error occurred during login",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> refreshToken(String refreshToken, HttpServletRequest request) {
        try {
            // Get existing token from database
            UserToken oldToken = userTokenRepository.findByTokenAndTokenType(refreshToken, TokenType.REFRESH)
                    .orElseThrow(() -> new Exception("Refresh token not found"));

            if (!jwtUtil.validateRefreshToken(refreshToken)) {
                oldToken.setExpired(true);
                userTokenRepository.save(oldToken);

                return ResponseFactory.error(
                        "Invalid refresh token",
                        HttpStatus.UNAUTHORIZED,
                        null
                );
            }

            String username = jwtUtil.getUsernameFromRefreshToken(oldToken.getToken());

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception("User not found"));

            Integer userId = user.getId();

            // Generate new access token
            String newAccessToken = jwtUtil.generateToken(username);
            LocalDateTime accessExpiredAt = jwtUtil.getAccessTokenExpiration(newAccessToken);

            // Generate new refresh token
            String newRefreshToken = jwtUtil.generateRefreshToken(username);
            LocalDateTime refreshExpiredAt = jwtUtil.getRefreshTokenExpiration(newRefreshToken);

            // Save new access token
            List<UserToken> tokens = List.of(
                    UserToken.builder()
                            .userId(userId)
                            .token(newAccessToken)
                            .tokenType(TokenType.ACCESS)
                            .expiredDate(accessExpiredAt)
                            .expired(false)
                            .revoked(false)
                            .build(),

                    UserToken.builder()
                            .userId(userId)
                            .token(newRefreshToken)
                            .tokenType(TokenType.REFRESH)
                            .expiredDate(refreshExpiredAt)
                            .expired(false)
                            .revoked(false)
                            .build()
            );
            userTokenRepository.saveAll(tokens);

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
            log.error("Refresh token failed: {}", ex.getMessage());
            return ResponseFactory.error(
                    "An error occurred while refreshing token",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    @Override
    public void logout(String refreshToken) {
        // Revoke refresh token
        userTokenRepository.findByTokenAndTokenType(refreshToken, TokenType.REFRESH)
                .ifPresent(oldToken -> {
                    oldToken.setRevoked(true);
                    userTokenRepository.save(oldToken);
                });


        // Revoke all access tokens for the user
        String username = jwtUtil.getUsernameFromRefreshToken(refreshToken);
        userRepository.findByUsername(username).ifPresent(user -> {
            List<UserToken> accessTokens = userTokenRepository.findAllByUserIdAndTokenTypeAndExpiredFalse(
                    user.getId(),
                    TokenType.ACCESS
            );

            accessTokens.forEach(token -> {
                token.setRevoked(true);
            });
            userTokenRepository.saveAll(accessTokens);
        });
    }
}
