package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/3/2025 9:41 PM
@Last Modified 12/3/2025 9:41 PM
Version 1.0
*/

import com.juaracoding.sikas.dto.response.Best10SellingProducts;
import com.juaracoding.sikas.model.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Long> {
    List<TransactionDetail> findByTransactionId(Long transactionId);

    @Query("""
        SELECT new com.juaracoding.sikas.dto.response.Best10SellingProducts(
            p.id,
            p.productName,
            CAST(SUM(td.qtyTransaction) AS integer)
        )
        FROM TransactionDetail td
        JOIN td.product p
        JOIN td.transaction t
        WHERE CAST(t.createdDate AS date) = CAST(GETDATE() AS date)
        GROUP BY p.id, p.productName
        ORDER BY p.id ASC
       """)
    List<Best10SellingProducts> findTodaysTop10BestSellingProducts();
}
