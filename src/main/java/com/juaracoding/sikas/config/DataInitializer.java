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

import com.juaracoding.sikas.model.*;
import com.juaracoding.sikas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRelationRepository userRelationRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;

    @Override
    public void run(String... args) {

        // --- Users & UserRelations ---
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

        User owner = userRepository.findByUsername("owneruser").orElseThrow();

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

            UserRelation userRelationAdmin = UserRelation.builder()
                    .ownerId(owner.getId())
                    .userId(adminUser.getId())
                    .createdBy(owner.getId())
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

            UserRelation userRelationCashier = UserRelation.builder()
                    .ownerId(owner.getId())
                    .userId(cashierUser.getId())
                    .createdBy(owner.getId())
                    .build();
            userRelationRepository.save(userRelationCashier);
        }

        User cashier = userRepository.findByUsername("cashieruser").orElseThrow();

        // --- Default Transactions ---
        if (transactionRepository.count() == 0) {

            Transaction txn1 = Transaction.builder()
                    .userId(cashier.getId())
                    .transactionNumber("TXN001")
                    .totalPriceTransaction(BigDecimal.valueOf(21000))
                    .createdDate(LocalDateTime.now())
                    .build();
            transactionRepository.save(txn1);

            Transaction txn2 = Transaction.builder()
                    .userId(cashier.getId())
                    .transactionNumber("TXN002")
                    .totalPriceTransaction(BigDecimal.valueOf(33000))
                    .createdDate(LocalDateTime.now())
                    .build();
            transactionRepository.save(txn2);

            // --- Default TransactionDetails ---
            TransactionDetail td1 = TransactionDetail.builder()
                    .transactionId(txn1.getId())
                    .productId(1L)
                    .qtyTransaction(2L)
                    .price(BigDecimal.valueOf(8000))
                    .totalPrice(BigDecimal.valueOf(16000))
                    .createdDate(LocalDateTime.now())
                    .createdBy(cashier.getId())
                    .build();

            TransactionDetail td2 = TransactionDetail.builder()
                    .transactionId(txn1.getId())
                    .productId(2L)
                    .qtyTransaction(1L)
                    .price(BigDecimal.valueOf(5000))
                    .totalPrice(BigDecimal.valueOf(5000))
                    .createdDate(LocalDateTime.now())
                    .createdBy(cashier.getId())
                    .build();

            TransactionDetail td3 = TransactionDetail.builder()
                    .transactionId(txn2.getId())
                    .productId(3L)
                    .qtyTransaction(3L)
                    .price(BigDecimal.valueOf(9000))
                    .totalPrice(BigDecimal.valueOf(27000))
                    .createdDate(LocalDateTime.now())
                    .createdBy(cashier.getId())
                    .build();

            TransactionDetail td4 = TransactionDetail.builder()
                    .transactionId(txn2.getId())
                    .productId(4L)
                    .qtyTransaction(2L)
                    .price(BigDecimal.valueOf(3000))
                    .totalPrice(BigDecimal.valueOf(6000))
                    .createdDate(LocalDateTime.now())
                    .createdBy(cashier.getId())
                    .build();

            transactionDetailRepository.save(td1);
            transactionDetailRepository.save(td2);
            transactionDetailRepository.save(td3);
            transactionDetailRepository.save(td4);
        }

    }
}