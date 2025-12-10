package com.juaracoding.sikas.service;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-253.28294.334, built on December 5, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/9/2025 11:47 PM
@Last Modified 12/9/2025 11:47 PM
Version 1.0
*/

import java.util.Map;

public interface MailerService {
    void sendTemplateEmail(String to, String subject, String templatePath, Map<String, String> variables);
}

