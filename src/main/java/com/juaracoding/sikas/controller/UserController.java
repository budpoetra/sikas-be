package com.juaracoding.sikas.controller;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/26/2025 20:37
@Last Modified 11/26/2025 20:37
Version 1.0
*/

import com.juaracoding.sikas.annotation.Loggable;
import com.juaracoding.sikas.dto.validation.UserDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.group.*;
import com.juaracoding.sikas.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Loggable
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createUser(@Validated(Create.class) @RequestBody UserDTO req) {
        return userService.createUser(req);
    }

    @Loggable
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @Loggable
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> updateUser(
            @PathVariable Integer id,
            @Validated(Update.class) @RequestBody UserDTO req
    ) {
        return userService.updateUser(id, req);
    }

    @Loggable
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @Loggable
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> getListUser(@RequestParam(value = "search", required = false) String search,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @RequestParam(value = "sort", defaultValue = "id") String sort,
                                                           @RequestParam(value = "direction", defaultValue = "desc") String direction,
                                                           HttpServletRequest request) {
        return userService.getListUser(search, page, size, sort, direction, request);
    }
}

