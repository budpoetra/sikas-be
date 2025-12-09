package com.juaracoding.sikas.security;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-253.28294.334, built on December 5, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/10/2025 1:18 AM
@Last Modified 12/10/2025 1:18 AM
Version 1.0
*/

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class RecaptchaServiceImpl {

    @Value("${google.recaptcha.secret-key}")
    private String recaptchaSecret;

    private static final String VERIFY_URL =
            "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyToken(String token) {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("secret", recaptchaSecret);   // ⬅️ SECRET KEY DIPAKAI DI SINI
        body.add("response", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(VERIFY_URL, request, Map.class);

        Map<String, Object> json = response.getBody();

        return json != null && Boolean.TRUE.equals(json.get("success"));
    }

}