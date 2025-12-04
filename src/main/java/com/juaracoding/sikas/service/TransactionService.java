package com.juaracoding.sikas.service;

import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.validation.TransactionDTO;
import com.juaracoding.sikas.dto.response.TransactionResponse;
import com.juaracoding.sikas.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionService {
//    TransactionResponse createTransaction(TransactionDTO request);
    ResponseEntity<ApiResponse<Object>> createTransaction(TransactionDTO transaction, HttpServletRequest request, UserDetailsImpl userDetails);
    TransactionResponse getTransactionById(Integer id);
    TransactionResponse getTransactionByTransactionNumber(String transactionNumber);
    List<TransactionResponse> getAllTransactions();
    List<TransactionResponse> getTransactionsByUserId(Integer userId);
}