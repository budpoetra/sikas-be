package com.juaracoding.sikas.repository;

import com.juaracoding.sikas.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findByTransactionNumber(String transactionNumber);
    boolean existsByTransactionNumber(String transactionNumber);
}
