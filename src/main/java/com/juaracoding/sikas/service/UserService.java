package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/25/2025 19:53
@Last Modified 11/25/2025 19:53
Version 1.0
*/

import com.juaracoding.sikas.dto.request.UserRequest;
import com.juaracoding.sikas.dto.response.ApiResponse;

public interface UserService {

    ApiResponse<?> createUser(UserRequest request);

    ApiResponse<?> getUser(Integer id);

    ApiResponse<?> updateUser(Integer id, UserRequest request);

    ApiResponse<?> deleteUser(Integer id);
}
