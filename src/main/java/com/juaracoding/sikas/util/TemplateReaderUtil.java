package com.juaracoding.sikas.util;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-253.28294.334, built on December 5, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/10/2025 12:17 AM
@Last Modified 12/10/2025 12:17 AM
Version 1.0
*/

import org.springframework.core.io.ClassPathResource;
import java.nio.charset.StandardCharsets;

public class TemplateReaderUtil {

    public static String readHtmlTemplate(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            byte[] bytes = resource.getInputStream().readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read template: " + path, e);
        }
    }

}