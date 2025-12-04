package com.juaracoding.sikas.controller;

import com.juaracoding.sikas.annotation.Loggable;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.AuthResponse;
import com.juaracoding.sikas.dto.validation.LoginDTO;
import com.juaracoding.sikas.dto.validation.TransactionDTO;
import com.juaracoding.sikas.dto.response.TransactionResponse;
import com.juaracoding.sikas.model.Transaction;
import com.juaracoding.sikas.model.User;
import com.juaracoding.sikas.security.UserDetailsImpl;
import com.juaracoding.sikas.service.TransactionService;
import com.juaracoding.sikas.util.DtoToModelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Loggable
    public ResponseEntity<ApiResponse<Object>> login(
            @Valid @RequestBody TransactionDTO trx,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {

//        return

        return transactionService.createTransaction(
                DtoToModelUtil.map(trx, TransactionDTO.class),
                request,
                userDetails
        );

//        return ResponseEntity.ok(serviceResponse);

//
//        ApiResponse<Object> body = serviceResponse.getBody();
//
//        if (body == null || body.getData() == null) {
//            return serviceResponse;
//        }
//
//        Map<String, Object> map = (Map<String, Object>) body.getData();
//
//        AuthResponse authResponse = (AuthResponse) map.get("authResponse");
//
//        ApiResponse<Object> finalResponse = new ApiResponse<>(
//                body.isSuccess(),
//                body.getMessage(),
//                body.getStatus(),
//                authResponse
//        );
//
//        return new ResponseEntity<>(finalResponse, serviceResponse.getStatusCode());
    }


}