package com.juaracoding.sikas.repository;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/15/2025 4:43 PM
@Last Modified 11/15/2025 4:43 PM
Version 1.0
*/

import com.juaracoding.sikas.model.MasterUserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterUserTypeRepository extends JpaRepository<MasterUserType, Integer> {
    MasterUserType findByUserType(String userType);
}