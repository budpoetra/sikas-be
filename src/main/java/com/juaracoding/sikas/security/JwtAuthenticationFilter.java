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

import com.juaracoding.sikas.model.UserToken;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

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

        if (!userTokenService.isTokenSafe(token)) {
            logger.info("Unsafe token detected: " + token);

            ResponseFactory.errorFilter(
                    "Invalid or revoked token",
                    HttpServletResponse.SC_UNAUTHORIZED,
                    response
            );
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            userTokenService.findByToken(token)
                    .ifPresent(ut -> {
                        ut.setExpired(true);
                        userTokenService.save(ut);
                    });

            ResponseFactory.errorFilter(
                    "Invalid or expired token",
                    HttpServletResponse.SC_UNAUTHORIZED,
                    response
            );
            return;
        }

        String username = jwtUtil.getUsername(token);
        if (username == null) {
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