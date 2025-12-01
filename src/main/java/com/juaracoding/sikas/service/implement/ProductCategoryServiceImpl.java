package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 11:56
@Last Modified 11/23/2025 11:56
Version 1.0
*/

import com.juaracoding.sikas.dto.request.ProductCategoryRequest;
import com.juaracoding.sikas.dto.response.ProductCategoryResponse;
import com.juaracoding.sikas.model.ProductCategory;
import com.juaracoding.sikas.repository.ProductCategoryRepository;
import com.juaracoding.sikas.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository repository;

    @Override
    public ProductCategoryResponse create(ProductCategoryRequest request) {

        ProductCategory entity = new ProductCategory();
        entity.setCategory(request.getCategory());
        entity.setCreatedBy(1L);
        entity.setUpdatedBy(1L);


        ProductCategory saved = repository.save(entity);

        return mapToResponse(saved);
    }

    @Override
    public ProductCategoryResponse update(Long id, ProductCategoryRequest request) {

        ProductCategory entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        entity.setCategory(request.getCategory());
        entity.setUpdatedBy(1L);

        ProductCategory updated = repository.save(entity);

        return mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ProductCategoryResponse getOne(Long id) {

        ProductCategory entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return mapToResponse(entity);
    }

    @Override
    public List<ProductCategoryResponse> getAll() {

        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductCategoryResponse mapToResponse(ProductCategory entity) {
        return ProductCategoryResponse.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}




