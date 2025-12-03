package com.juaracoding.sikas.config;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 1:28 AM
@Last Modified 12/4/2025 1:28 AM
Version 1.0
*/

import com.juaracoding.sikas.util.AuditorAwareUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditorAwareConfig {

    @Bean
    public AuditorAware<Integer> auditorProvider() {
        return new AuditorAwareUtil();
    }
}