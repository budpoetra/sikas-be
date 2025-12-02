package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/25/2025 19:53
@Last Modified 11/25/2025 19:53
Version 1.0
*/
import com.juaracoding.sikas.dto.validation.UserDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.MasterUserTypeResponse;
import com.juaracoding.sikas.dto.response.UserResponse;
import com.juaracoding.sikas.model.MasterUserType;
import com.juaracoding.sikas.model.User;
import com.juaracoding.sikas.repository.MasterUserTypeRepository;
import com.juaracoding.sikas.repository.UserRepository;
import com.juaracoding.sikas.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MasterUserTypeRepository masterUserTypeRepository;
    private final PasswordEncoder passwordEncoder;

    // ========================= CREATE =========================
    @Override
    public ApiResponse<?> createUser(UserDTO request) {

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            return new ApiResponse<>(false, "Password is required", 400, null);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponse<>(false, "Username already exists", 400, null);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse<>(false, "Email already exists", 400, null);
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            return new ApiResponse<>(false, "Phone already exists", 400, null);
        }

        MasterUserType userType =
                masterUserTypeRepository.findById(request.getTypeId()).orElse(null);

        User user = new User();
        user.setTypeId(request.getTypeId());
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);

        userRepository.save(user);

        return new ApiResponse<>(true, "User created successfully", 201,
                mapToResponse(user, userType));
    }

    // ========================= GET =========================
    @Override
    public ApiResponse<?> getUser(Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    MasterUserType userType = masterUserTypeRepository
                            .findById(user.getTypeId()).orElse(null);
                    return new ApiResponse<>(true, "Success", 200,
                            mapToResponse(user, userType));
                })
                .orElseGet(() -> new ApiResponse<>(false, "User not found", 404, null));
    }

    // ========================= UPDATE =========================
    @Override
    public ApiResponse<?> updateUser(Integer id, UserDTO req) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new ApiResponse<>(false, "User not found", 404, null);
        }

        // Cek email unique
        if (!user.getEmail().equals(req.getEmail()) &&
                userRepository.existsByEmail(req.getEmail())) {
            return new ApiResponse<>(false, "Email already exists", 400, null);
        }

        // Cek phone unique
        if (!user.getPhone().equals(req.getPhone()) &&
                userRepository.existsByPhone(req.getPhone())) {
            return new ApiResponse<>(false, "Phone already exists", 400, null);
        }

        // Username sebaiknya tidak diubah
        req.setUsername(user.getUsername());

        user.setTypeId(req.getTypeId());
        user.setFullName(req.getFullName());
        user.setPhone(req.getPhone());
        user.setEmail(req.getEmail());
        user.setStatus(req.getStatus());

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        userRepository.save(user);

        MasterUserType userType = masterUserTypeRepository
                .findById(req.getTypeId()).orElse(null);

        return new ApiResponse<>(true, "User updated successfully", 200,
                mapToResponse(user, userType));
    }

    // ========================= DELETE =========================
    @Override
    public ApiResponse<?> deleteUser(Integer id) {

        if (!userRepository.existsById(id)) {
            return new ApiResponse<>(false, "User not found", 404, null);
        }

        userRepository.deleteById(id);

        return new ApiResponse<>(true, "User deleted successfully", 200, null);
    }

    // ========================= MAPPER =========================
    private UserResponse mapToResponse(User user, MasterUserType type) {
        MasterUserTypeResponse typeResp = null;

        if (type != null) {
            typeResp = MasterUserTypeResponse.builder()
                    .id(type.getId())
                    .userType(type.getUserType())
                    .createdDate(type.getCreatedDate())
                    .updatedDate(type.getUpdatedDate())
                    .build();
        }

        return UserResponse.builder()
                .id(user.getId())
                .typeId(user.getTypeId())
                .userType(typeResp) // <-- nested object
                .fullName(user.getFullName())
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .build();
    }

}





