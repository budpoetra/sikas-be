package com.juaracoding.sikas.dto.response;

/*
IntelliJ IDEA 2025.2.5 (Ultimate Edition)
Build #IU-252.28238.7, built on November 20, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 12/4/2025 6:10 PM
@Last Modified 12/4/2025 6:10 PM
Version 1.0
*/

import com.juaracoding.sikas.dto.LinksDTO;
import com.juaracoding.sikas.dto.MetaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    private T content;
    private MetaDTO meta;
    private LinksDTO links;
}
