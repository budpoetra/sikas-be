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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserTokenRepository userTokenRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           UserTokenRepository userTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userTokenRepository = userTokenRepository;
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
}
