package com.juaracoding.sikas.security;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/15/2025 11:24 AM
@Last Modified 11/15/2025 11:24 AM
Version 1.0
*/

import com.juaracoding.sikas.service.UserTokenService;
import com.juaracoding.sikas.util.ResponseFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JWT Authentication Filter
 * Platform Code: JAF
 * Module Code: 000
 * Quota Code: -
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserTokenService userTokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = getJwtFromRequest(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if(!userTokenService.isTokenSafe(token)) {
            log.warn("JAF000W01 - Token is revoked or expired");

            ResponseFactory.errorFilter(
                    "Token is revoked or expired",
                    HttpServletResponse.SC_UNAUTHORIZED,
                    response
            );
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            log.warn("JAF000W02 - Invalid or expired token");

            ResponseFactory.errorFilter(
                    "Invalid or expired token",
                    HttpServletResponse.SC_UNAUTHORIZED,
                    response
            );
            return;
        }

        String username = jwtUtil.getUsername(token);
        if (username == null) {
            log.warn("JAF000W03 - Invalid token structure");

            ResponseFactory.errorFilter(
                    "Invalid token structure",
                    HttpServletResponse.SC_UNAUTHORIZED,
                    response
            );
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token signature + expiration
            if (jwtUtil.isValidToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        return null;
    }
}