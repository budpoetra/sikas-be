package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/27/2025 4:02 PM
@Last Modified 11/27/2025 4:02 PM
Version 1.0
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private Integer id;
    private Integer typeId;
    private List<String> userType;
    private String fullName;
    private String username;
    private String phone;
    private String email;
    private Byte status;
    private String statusName;
    private Map<String, List<String>> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}