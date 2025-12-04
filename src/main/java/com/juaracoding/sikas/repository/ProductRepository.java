package com.juaracoding.sikas.repository;

import com.juaracoding.sikas.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {


    // List<Product> findAllById(Set<Integer> productIds);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllById(@Param("ids") List<Long> ids);
}
