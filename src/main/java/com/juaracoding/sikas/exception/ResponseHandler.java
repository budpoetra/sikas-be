package com.juaracoding.sikas.handler;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/16/2025 1:16 PM
@Last Modified 11/16/2025 1:16 PM
Version 1.0
*/

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public ResponseEntity<Object> handleResponse(
            String message,
            HttpStatus status,
            Object data,
            Object errorCode,
            HttpServletRequest request
    ){

        Map<String,Object> m = new HashMap<>();
        m.put("message",message);
        m.put("status",status.value());
        m.put("data",data==null?"":data);
        m.put("timestamp", Instant.now().toString());
        m.put("success",!status.isError());
        if(errorCode!=null){
            m.put("error_code",errorCode);
            m.put("path",request.getRequestURI());
        }
        return new ResponseEntity<>(m,status);
    }

    public ResponseEntity<Object> handleResponse(
            String message,
            HttpStatus status,
            Object data,
            Object errorCode,
            WebRequest request
    ){

        Map<String,Object> m = new HashMap<>();
        m.put("message",message);
        m.put("status",status.value());
        m.put("data",data==null?"":data);
        m.put("timestamp", Instant.now().toString());
        m.put("success",!status.isError());
        if(errorCode!=null){
            m.put("error_code",errorCode);
            m.put("path",request.getContextPath());
        }
        return new ResponseEntity<>(m,status);
    }
}