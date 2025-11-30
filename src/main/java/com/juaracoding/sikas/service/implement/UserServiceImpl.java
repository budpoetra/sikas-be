package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/27/2025 3:38 PM
@Last Modified 11/27/2025 3:38 PM
Version 1.0
*/

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.ProfileResponse;
import com.juaracoding.sikas.dto.validation.ChangePasswordDTO;
import com.juaracoding.sikas.model.MasterUserType;
import com.juaracoding.sikas.model.User;
import com.juaracoding.sikas.model.UserTypePermission;
import com.juaracoding.sikas.repository.MasterUserTypeRepository;
import com.juaracoding.sikas.repository.UserRepository;
import com.juaracoding.sikas.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Find user by username
     * Platform Code: UST
     * Module Code: 003
     * Quota Code: 01 - 10
     */
    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            log.error("USR003E10 - Error finding user by username: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Get user profile by username
     * Platform Code: UST
     * Module Code: 003
     * Quota Code: 11 - 20
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> getProfile(String username) {
        try {
            Optional<User> userOpt = userRepository.fetchUserAndPermissionsByUsername(username);

            if (userOpt.isEmpty()) {
                log.warn("USR003W11 - User not found for username: {}", username);

                ApiResponse<Object> response = new ApiResponse<>(
                        false,
                        "USR003W11 - User not found",
                        HttpStatus.NOT_FOUND.value(),
                        null
                );

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User user = userOpt.get();

            Map<String, List<String>> permissionMap =
                    user.getMasterUserType()
                            .getPermissions()
                            .stream()
                            .filter(UserTypePermission::getIsAllowed)
                            .collect(Collectors.groupingBy(
                                    p -> p.getMasterAction().getName().toLowerCase(),
                                    Collectors.mapping(
                                            p -> p.getMasterFeature().getName().toLowerCase(),
                                            Collectors.toList()
                                    )
                            ));


            ProfileResponse profile = ProfileResponse.builder()
                    .id(user.getId())
                    .typeId(user.getTypeId())
                    .userType(List.of(user.getMasterUserType().getUserType()))
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .status(user.getStatus())
                    .statusName(user.getStatusName())
                    .permissions(permissionMap)
                    .createdAt(user.getCreatedDate())
                    .updatedAt(user.getUpdatedDate())
                    .build();

            ApiResponse<Object> response = new ApiResponse<>(
                    true,
                    "Profile retrieved successfully",
                    HttpStatus.OK.value(),
                    profile
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("USR003E20 - Error retrieving profile: {}", e.getMessage());

            ApiResponse<Object> response = new ApiResponse<>(
                    false,
                    "USR003E20 - Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Change user password
     * Platform Code: UST
     * Module Code: 003
     * Quota Code: 21 - 30
     */
    public  ResponseEntity<ApiResponse<Object>> changePassword(ChangePasswordDTO dto, String username,  HttpServletRequest request) {
        try {
            if(!dto.getNewPassword().equals(dto.getConfirmPassword())) {
                log.warn("USR003W21 - New password and confirm password do not match for username: {}", username);

                ApiResponse<Object> response = new ApiResponse<>(
                        false,
                        "USR003W21 - New password and confirm password do not match",
                        HttpStatus.BAD_REQUEST.value(),
                        null
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Optional<User> userOpt = userRepository.findByUsername(username);
            if(userOpt.isEmpty()) {
                log.warn("USR003W22 - User not found for username: {}", username);

                ApiResponse<Object> response = new ApiResponse<>(
                        false,
                        "USR003W22 - User not found",
                        HttpStatus.NOT_FOUND.value(),
                        null
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            User user = userOpt.get();

            if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
                log.warn("USR003W23 - Old password is incorrect for username: {}", username);

                ApiResponse<Object> response = new ApiResponse<>(
                        false,
                        "USR003W23 - Old password is incorrect",
                        HttpStatus.BAD_REQUEST.value(),
                        null
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);

            ApiResponse<Object> response = new ApiResponse<>(
                    true,
                    "Password changed successfully",
                    HttpStatus.OK.value(),
                    null
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("USR003E30 - Error changing password for username {}: {}", username, e.getMessage());

            ApiResponse<Object> response = new ApiResponse<>(
                    false,
                    "USR003E30 - Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}