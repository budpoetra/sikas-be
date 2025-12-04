package com.juaracoding.sikas.util;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 6:05 PM
@Last Modified 12/4/2025 6:05 PM
Version 1.0
*/

import jakarta.servlet.http.HttpServletRequest;

public class PaginationUrlBuilderUtil {

    public static String build(
            HttpServletRequest request,
            int targetPage
    ) {
        String baseUrl = request.getRequestURL().toString();
        String query = request.getQueryString();

        // Hilangkan page=xxx dari query existing
        if (query != null) {
            query = query.replaceAll("(&?page=\\d+)", "");
        }

        String q = (query == null || query.isBlank())
                ? ""
                : "&" + query;

        return baseUrl + "?page=" + targetPage + q;
    }

    public static String buildNullable(
            HttpServletRequest request,
            int targetPage,
            int totalPages
    ) {
        if (targetPage < 0 || targetPage >= totalPages) return null;
        return build(request, targetPage);
    }
}