package com.juaracoding.sikas.service;

import com.juaracoding.sikas.model.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionDetailService extends JpaRepository<TransactionDetail, Long> {
    List<TransactionDetail> findByTransactionId(Long transactionId);
}