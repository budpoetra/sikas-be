package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/27/2025 3:38 PM
@Last Modified 11/27/2025 3:38 PM
Version 1.0
*/

import com.juaracoding.sikas.dto.validation.UserDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.ChangePasswordDTO;
import com.juaracoding.sikas.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * Platform Code: USR
 * Module Code: 003
 */
public interface UserService {

    Optional<User> findByUsername(String username);

    ResponseEntity<ApiResponse<Object>> getProfile(String username);

    ResponseEntity<ApiResponse<Object>> changePassword(ChangePasswordDTO dto, String username, HttpServletRequest request);

    ResponseEntity<ApiResponse<Object>> createUser(UserDTO request);

    ResponseEntity<ApiResponse<Object>> getUser(Integer id);

    ResponseEntity<ApiResponse<Object>> updateUser(Integer id, UserDTO request);

    ResponseEntity<ApiResponse<Object>> deleteUser(Integer id);

    ResponseEntity<ApiResponse<Object>> getListUser(
            String search,
            int page,
            int size,
            String sort,
            String direction,
            HttpServletRequest request
    );
}
