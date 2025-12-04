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

import com.juaracoding.sikas.dto.LinksDTO;
import com.juaracoding.sikas.dto.response.*;
import com.juaracoding.sikas.dto.validation.ChangePasswordDTO;
import com.juaracoding.sikas.dto.MetaDTO;
import com.juaracoding.sikas.dto.validation.UserDTO;
import com.juaracoding.sikas.model.MasterUserType;
import com.juaracoding.sikas.model.User;
import com.juaracoding.sikas.model.UserRelation;
import com.juaracoding.sikas.model.UserTypePermission;
import com.juaracoding.sikas.repository.MasterUserTypeRepository;
import com.juaracoding.sikas.repository.UserRelationRepository;
import com.juaracoding.sikas.repository.UserRepository;
import com.juaracoding.sikas.security.UserDetailsImpl;
import com.juaracoding.sikas.service.UserService;
import com.juaracoding.sikas.util.PaginationUrlBuilderUtil;
import com.juaracoding.sikas.util.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MasterUserTypeRepository masterUserTypeRepository;
    private final UserRelationRepository userRelationRepository;
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
    @Override
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

    /**
     * Create new user
     * Platform Code: UST
     * Module Code: 003
     * Quota Code: 31 - 40
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> createUser(UserDTO request) {

        try {
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                log.warn("USR003W31 - Password is empty");

                return ResponseFactory.error(
                        "USR003W31 - Password is empty",
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }

            if (userRepository.existsByUsername(request.getUsername())) {
                log.warn("USR003W32 - Username already exists");

                return ResponseFactory.error(
                        "USR003W32 - Username already exists",
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                log.warn("USR003W33 - Email already exists");

                return ResponseFactory.error(
                        "USR003W33 - Email already exists",
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }

            if (userRepository.existsByPhone(request.getPhone())) {
                log.warn("USR003W34 - Phone already exists");

                return ResponseFactory.error(
                        "USR003W34 - Phone already exists",
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }

            if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
                log.warn("USR003W35 - Password and Confirm Password do not match");

                return ResponseFactory.error(
                        "USR003W35 - Password and Confirm Password do not match",
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }

            MasterUserType userType = masterUserTypeRepository
                    .findById(request.getTypeId()).orElseThrow(() -> {
                        log.warn("USR003W36 - User Type not found");

                        return new RuntimeException("USR003W35 - User Type not found");
                    });

            User user = new User();
            user.setTypeId(request.getTypeId());
            user.setFullName(request.getFullName());
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setStatus(request.getStatus() != null ? request.getStatus() : 1);

            userRepository.save(user);

            Authentication auth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            UserDetailsImpl authUser = (UserDetailsImpl) auth.getPrincipal();

            UserRelation userRelation = new UserRelation();
            userRelation.setOwnerId(authUser.getUser().getId());
            userRelation.setUserId(user.getId());
            userRelationRepository.save(userRelation);

            return ResponseFactory.success(
                    "User created successfully",
                    HttpStatus.CREATED,
                    mapToResponse(user, userType)
            );
        } catch (Exception e) {
            log.error("USR003E40 - Error creating user: {}", e.getMessage());

            return ResponseFactory.error(
                    "USR003E40 - Failed to create user",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    /**
     * Get user by ID
     * Platform Code: UST
     * Module Code: 003
     * Quota Code: 41 - 50
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> getUser(Integer id) {

        try {
            return userRepository.findById(id)
                    .map(user -> {
                        MasterUserType userType = masterUserTypeRepository
                                .findById(user.getTypeId())
                                .orElse(null);

                        ApiResponse<Object> response = new ApiResponse<>(
                                true,
                                "Success",
                                200,
                                mapToResponse(user, userType)
                        );

                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() -> {
                        log.warn("USR003E41 - User not found for id: {}", id);

                        ApiResponse<Object> response = new ApiResponse<>(
                                false,
                                "User not found",
                                404,
                                null
                        );

                        return ResponseEntity.status(404).body(response);
                    });
        } catch (NumberFormatException e) {
            log.error("USR003E50 - Error retrieving user: {}", e.getMessage());

            ApiResponse<Object> response = new ApiResponse<>(
                    false,
                    "Invalid user ID format",
                    400,
                    null
            );
            return ResponseEntity.status(400).body(response);
        }
    }

    /**
     * Update user by ID
     * Platform Code: UST
     * Module Code: 003
     * Quota Code: 51 - 60
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponse<Object>> updateUser(
            Integer id,
            UserDTO req
    ) {
        try {

            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                log.warn("USR003W51 - User not found for id: {}", id);

                return ResponseFactory.error(
                        "USR003W41 - User not found",
                        HttpStatus.NOT_FOUND,
                        null
                );
            }

            boolean emailExists = userRepository.existsByEmailAndIdNot(req.getEmail(), id);
            if (emailExists) {
                log.warn("USR003W52 - Email already exists: {}", req.getEmail());

                return ResponseFactory.error(
                        "Email already exists",
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }

            boolean phoneExists = userRepository.existsByPhoneAndIdNot(req.getPhone(), id);
            if (phoneExists) {
                log.warn("USR003W53 - Phone already exists: {}", req.getPhone());

                return ResponseFactory.error(
                        "Phone already exists",
                        HttpStatus.BAD_REQUEST,
                        null
                );
            }

            user.setFullName(req.getFullName());
            user.setPhone(req.getPhone());
            user.setEmail(req.getEmail());

            userRepository.save(user);

            MasterUserType userType = masterUserTypeRepository
                    .findById(user.getTypeId())
                    .orElse(null);

            return ResponseFactory.success(
                    "User updated successfully",
                    HttpStatus.OK,
                    mapToResponse(user, userType)
            );

        } catch (Exception e) {
            log.error("USR003E60 - Error updating user: {}", e.getMessage(), e);

            return ResponseFactory.error(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }

    /**
     * Delete user by ID
     * Platform Code: UST
     * Module Code: 003
     * Quota Code: 61 - 70
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> deleteUser(Integer id) {

        try {
            Optional<User> userOpt = userRepository.findById(id);

            if (userOpt.isEmpty()) {
                log.warn("USR003W61 - User not found for id: {}", id);

                return ResponseFactory.error(
                        "USR003W61 - User not found",
                        HttpStatus.NOT_FOUND,
                        null
                );
            }

            User user = userOpt.get();

            user.setStatus((byte) 0);
            userRepository.save(user);

            return ResponseFactory.success(
                    "User deleted successfully",
                    HttpStatus.OK,
                    null
            );
        } catch (NumberFormatException e) {
            log.error("USR003W70 - Invalid user ID format: {}", e.getMessage());

            return ResponseFactory.error(
                    "USR003W70 - Invalid user ID format",
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Object>> getListUser(
            String search,
            int page,
            int size,
            String sort,
            String direction,
            HttpServletRequest request
    ) {
        try {
            Authentication auth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            UserDetailsImpl authUser = (UserDetailsImpl) auth.getPrincipal();

            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    direction.equalsIgnoreCase("desc")
                            ? Sort.by(sort).descending()
                            : Sort.by(sort).ascending()
            );

            Page<User> userPage = userRepository.searchListUsers(authUser.getUser().getId(), search, pageable);

            List<UserListResponse> content = userPage.getContent()
                    .stream()
                    .map(u -> new UserListResponse(
                            u.getId(),
                            u.getFullName(),
                            u.getUsername(),
                            u.getEmail(),
                            u.getPhone(),
                            u.getMasterUserType().getUserType()
                    ))
                    .toList();

            int totalPages = userPage.getTotalPages();

            String selfUrl = PaginationUrlBuilderUtil.build(request, page);
            String nextUrl = PaginationUrlBuilderUtil.buildNullable(request, page + 1, totalPages);
            String prevUrl = PaginationUrlBuilderUtil.buildNullable(request, page - 1, totalPages);

            MetaDTO meta = new MetaDTO(
                    userPage.getNumber() + 1,
                    userPage.getSize(),
                    userPage.getTotalElements(),
                    userPage.getTotalPages()
            );

            LinksDTO links = new LinksDTO(
                    selfUrl,
                    nextUrl,
                    prevUrl
            );

            PageResponse<Object> response = new PageResponse<>(
                    content,
                    meta,
                    links
            );

            return ResponseFactory.success(
                    "User list retrieved successfully",
                    HttpStatus.OK,
                    response
            );
        } catch (Exception e) {
            log.error("USR003E80 - Error retrieving user list: {}", e.getMessage());

            return ResponseFactory.error(
                    "USR003E80 - Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }

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