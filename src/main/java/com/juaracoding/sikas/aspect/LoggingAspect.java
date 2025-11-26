package com.juaracoding.sikas.aspect;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/22/2025 1:48 PM
@Last Modified 11/22/2025 1:48 PM
Version 1.0
*/

import com.juaracoding.sikas.annotation.Loggable;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper mapper;

    @Value("${logging.mask-sensitive:true}")
    private boolean maskSensitive;

    public LoggingAspect(ObjectMapper mapper) {
        this.mapper = mapper.copy();
    }

    @Around("@annotation(loggable)")
    public Object logExecution(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {

        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String service = joinPoint.getTarget().getClass().getName();
        String method = joinPoint.getSignature().getName();

        HttpServletRequest servletRequest = extractServletRequest(joinPoint.getArgs());

        // Log Request
        log.info("[{}::{}] Request -> {}", service, method, buildRequestLog(servletRequest, joinPoint.getArgs()));

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            // Log Response
            log.info("[{}::{}] Response -> {} ({} ms)", service, method, buildResponseLog(result), duration);

            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("[{}::{}] Error -> {} ({} ms)", service, method, ex.getMessage(), duration);
            throw ex;
        } finally {
            MDC.remove("traceId");
        }
    }

    // ========================
    // Request Logging
    // ========================
    private String buildRequestLog(HttpServletRequest request, Object[] args) {
        Map<String, Object> map = new LinkedHashMap<>();

        // Headers
        map.put("headers", request != null ? getHeadersMap(request) : "<no-servlet-request>");

        // Body
        map.put("body", parseArgsToBody(args));

        return safeToJson(map);
    }

    private Map<String, String> getHeadersMap(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, maskSensitiveHeader(name, request.getHeader(name)));
        }
        return headers;
    }

    private Object parseArgsToBody(Object[] args) {
        List<Object> list = new ArrayList<>();
        for (Object arg : args) {
            Object masked = maskRequestArgs(arg);
            if (masked != null) list.add(masked);
        }
        return list.size() == 1 ? list.getFirst() : list;
    }

    private Object maskRequestArgs(Object arg) {
        if (arg == null) return null;
        if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) return "<skipped-servlet>";
        if (arg instanceof Authentication || arg instanceof UserDetails) return "<skipped-security>";

        try {
            String json = mapper.writeValueAsString(arg);
            return mapper.readValue(maskSensitiveJson(json), Object.class);
        } catch (Exception e) {
            return "<unable-to-mask>";
        }
    }

    private HttpServletRequest extractServletRequest(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest request) return request;
        }
        return null;
    }

    private String maskSensitiveHeader(String name, String value) {
        if (value == null || !maskSensitive) return value;

        if ("authorization".equalsIgnoreCase(name)) return "Bearer ******";
        if ("cookie".equalsIgnoreCase(name))
            return value.replaceAll("(?i)refresh_token=[^;]+", "refresh_token=******");

        return value;
    }

    // ========================
    // Response Logging
    // ========================
    private String buildResponseLog(Object result) {
        Map<String, Object> map = new LinkedHashMap<>();

        if (result instanceof ResponseEntity<?> responseEntity) {
            map.put("headers", getResponseHeadersMap(responseEntity));
            map.put("body", maskObject(responseEntity.getBody()));
        } else {
            map.put("headers", "<unknown>");
            map.put("body", maskObject(result));
        }

        return safeToJson(map);
    }

    private Map<String, String> getResponseHeadersMap(ResponseEntity<?> responseEntity) {
        Map<String, String> headers = new HashMap<>();
        responseEntity.getHeaders().forEach((key, values) -> {
            if (!values.isEmpty()) headers.put(key, values.getFirst());
        });
        return headers;
    }

    private Object maskObject(Object obj) {
        if (obj == null) return null;
        try {
            String json = mapper.writeValueAsString(obj);
            return mapper.readValue(maskSensitiveJson(json), Object.class);
        } catch (Exception e) {
            return "<unable-to-mask>";
        }
    }

    private String maskSensitiveJson(String json) {
        if (!maskSensitive) return json;

        json = json.replaceAll("(?i)\"password\"\\s*:\\s*\"(.*?)\"", "\"password\":\"*******\"");
        json = json.replaceAll("(?i)\"token\"\\s*:\\s*\"(.*?)\"", "\"token\":\"******\"");
        json = json.replaceAll("(?i)\"refreshToken\"\\s*:\\s*\"(.*?)\"", "\"refreshToken\":\"******\"");
        return json;
    }

    private String safeToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "<unable-to-serialize>";
        }
    }
}

