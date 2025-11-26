package com.juaracoding.sikas.security;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/15/2025 11:23 AM
@Last Modified 11/15/2025 11:23 AM
Version 1.0
*/

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key accessKey;
    private final Key refreshKey;

    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long accessExpirationMs,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.refresh-expiration}") long refreshExpirationMs
    ) {
        this.accessKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = accessExpirationMs;

        this.refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generateToken(String username) {
        return buildToken(username, accessExpirationMs, accessKey);
    }

    public String generateRefreshToken(String username) {
        return buildToken(username, refreshExpirationMs, refreshKey);
    }

    private String buildToken(String username, long expirationMs, Key key) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public LocalDateTime getAccessTokenExpiration(String token) {
        Claims claims = getClaims(token, accessKey);

        return LocalDateTime.ofInstant(
                claims.getExpiration().toInstant(),
                ZoneId.systemDefault()
        );
    }

    public LocalDateTime getRefreshTokenExpiration(String token) {
        Claims claims = getClaims(token, refreshKey);

        return LocalDateTime.ofInstant(
                claims.getExpiration().toInstant(),
                ZoneId.systemDefault()
        );
    }

    public String getUsername(String token) {
        return getClaims(token, accessKey).getSubject();
    }

    public String getUsernameFromRefreshToken(String token) {
        return getClaims(token, refreshKey).getSubject();
    }

    public boolean isValidToken(String token, String username) {
        return username.equals(getUsername(token)) && !isExpired(token);
    }

    public boolean isExpired(String token) {
        return getClaims(token, accessKey).getExpiration().before(new Date());
    }

    private Claims getClaims(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        return parseToken(token, accessKey);
    }

    public boolean validateRefreshToken(String token) {
        return parseToken(token, refreshKey);
    }

    private boolean parseToken(String token, Key key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}