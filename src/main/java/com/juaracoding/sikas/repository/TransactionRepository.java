package com.juaracoding.sikas.repository;

import com.juaracoding.sikas.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    // Optional<Transaction> findByTransactionNumber(String transactionNumber);
    // boolean existsByTransactionNumber(String transactionNumber);

    Optional<Transaction> findByTransactionNumber(String transactionNumber);

    @Query("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.transactionDetails WHERE t.userId = :userId")
    List<Transaction> findByUserIdWithDetails(@Param("userId") Integer userId);

    @Query("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.transactionDetails")
    List<Transaction> findAllWithDetails();

    Page<Transaction> findByUserId(Integer userId, Pageable pageable);
}
