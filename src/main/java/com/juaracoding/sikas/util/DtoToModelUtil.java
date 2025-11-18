package com.juaracoding.sikas.util;

/*
IntelliJ IDEA 2025.2.4 (Ultimate Edition)
Build #IU-252.27397.103, built on October 23, 2025
@Author budpoetra a.k.a. Budi Sahputra
Java Developer
Created on 11/17/2025 4:29 PM
@Last Modified 11/17/2025 4:29 PM
Version 1.0
*/

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapToModelUtil {

    private static ModelMapper modelMapper = new ModelMapper();

    public MapToModelUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Map DTO to Model (generic)
     * @param source Object source (DTO)
     * @param targetClass Target class (Entity or Model)
     * @return instance of the target class
     * @param <S> Source class
     * @param <T> Target class
     */
    public static <S, T> T mapTo(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }
}