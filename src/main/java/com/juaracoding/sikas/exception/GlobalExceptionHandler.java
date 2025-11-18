package com.juaracoding.sikas.exception;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/16/2025 12:06 PM
@Last Modified 11/16/2025 12:06 PM
Version 1.0
*/

import com.juaracoding.sikas.util.LoggingFile;
import com.juaracoding.sikas.util.RequestCapture;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Configuration
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    private static final String CLASS_NAME = "";

    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, HttpServletRequest request) {
        LoggingFile.logException(CLASS_NAME,"handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, HttpServletRequest request) "+ RequestCapture.allRequest(request),ex);
        return new ResponseHandler().handleResponse("Terjadi Kesalahan Di Server",status,null,"X05999", request);
    }

}