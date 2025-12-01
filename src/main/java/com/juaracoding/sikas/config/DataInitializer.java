package com.juaracoding.sikas.config;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/15/2025 11:35 AM
@Last Modified 11/15/2025 11:35 AM
Version 1.0
*/

import com.juaracoding.sikas.model.User;
import com.juaracoding.sikas.model.UserRelation;
import com.juaracoding.sikas.repository.UserRelationRepository;
import com.juaracoding.sikas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (!userRepository.existsByUsername("super-admin")) {
            User superAdmin = User.builder()
                    .typeId(1)
                    .fullName("Super Admin")
                    .username("superadmin")
                    .password(passwordEncoder.encode("12345qwert@Q"))
                    .phone("1234567890")
                    .email("superadmin@sikas.com")
                    .status((byte) 1)
                    .build();

            userRepository.save(superAdmin);
        }

        if (!userRepository.existsByUsername("owneruser")) {
            User ownerUser = User.builder()
                    .typeId(2)
                    .fullName("Owner User")
                    .username("owneruser")
                    .password(passwordEncoder.encode("12345qwert@Q"))
                    .phone("0987654321")
                    .email("owneruser@sikas.com")
                    .status((byte) 1)
                    .build();

            userRepository.save(ownerUser);
        }

        if (!userRepository.existsByUsername("adminuser")) {
            User adminUser = User.builder()
                    .typeId(3)
                    .fullName("Admin User")
                    .username("adminuser")
                    .password(passwordEncoder.encode("12345qwert@Q"))
                    .phone("1122334455")
                    .email("adminuser@sikas.com")
                    .status((byte) 1)
                    .build();

            userRepository.save(adminUser);

            // Create UserRelation for admin
            UserRelation userRelationAdmin = UserRelation.builder()
                    .ownerId(2)
                    .userId(adminUser.getId())
                    .createdBy(2)
                    .build();

            userRelationRepository.save(userRelationAdmin);
        }

        if (!userRepository.existsByUsername("cashieruser")) {
            User cashierUser = User.builder()
                    .typeId(4)
                    .fullName("Cashier User")
                    .username("cashieruser")
                    .password(passwordEncoder.encode("12345qwert@Q"))
                    .phone("5566778899")
                    .email("cashieruser@sikas.com")
                    .status((byte) 1)
                    .build();

            userRepository.save(cashierUser);

            // Create UserRelation for cashier
            UserRelation userRelationCashier = UserRelation.builder()
                    .ownerId(2)
                    .userId(cashierUser.getId())
                    .createdBy(2)
                    .build();

            userRelationRepository.save(userRelationCashier);
        }

    }
}