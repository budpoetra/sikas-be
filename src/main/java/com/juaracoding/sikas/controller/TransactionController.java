package com.juaracoding.sikas.controller;

import com.juaracoding.sikas.annotation.Loggable;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.TransactionDTO;
import com.juaracoding.sikas.security.UserDetailsImpl;
import com.juaracoding.sikas.service.TransactionService;
import com.juaracoding.sikas.util.DtoToModelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Loggable
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createTransactions(
            @Valid @RequestBody TransactionDTO trx,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {

        return transactionService.createTransaction(
                DtoToModelUtil.map(trx, TransactionDTO.class),
                request,
                userDetails
        );

    }

    @Loggable
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> searchProductForTransaction(@RequestParam(required = false) String search) {
        return transactionService.getProductForTransaction(search);
    }

}