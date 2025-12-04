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

import com.juaracoding.sikas.dto.LinksDTO;
import com.juaracoding.sikas.dto.MetaDTO;
import com.juaracoding.sikas.dto.response.ApiResponse;
import com.juaracoding.sikas.dto.response.PageResponse;
import com.juaracoding.sikas.dto.validation.ProductCategoryDTO;
import com.juaracoding.sikas.dto.response.ProductCategoryResponse;
import com.juaracoding.sikas.model.ProductCategory;
import com.juaracoding.sikas.repository.ProductCategoryRepository;
import com.juaracoding.sikas.service.ProductCategoryService;
import com.juaracoding.sikas.util.PaginationUrlBuilderUtil;
import com.juaracoding.sikas.util.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository repository;

    /**
     * Create Product Category
     * Platform Code: PDC
     * Module Code: 005
     * Quota Code: 01 - 10
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> create(ProductCategoryDTO request) {

        try {
            if (repository.existsByCategory(request.getCategory())) {
                log.error("PDC005E01 - Category {} already exists", request.getCategory());

                return ResponseFactory.error(
                        "PDC005E01 - Category already exists",
                        HttpStatus.CONFLICT,
                        null
                );
            }

            ProductCategory entity = new ProductCategory();
            entity.setCategory(request.getCategory());

            ProductCategory saved = repository.save(entity);

            return ResponseFactory.success(
                    "Category created successfully",
                    HttpStatus.CREATED,
                    mapToResponse(saved)
            );
        } catch (Exception e) {
            log.error("PDC005E10 - Error creating category: {}", e.getMessage());

            return ResponseFactory.error(
                    "PDC005E10 - Failed to create category",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }

    }

    /**
     * Update Product Category
     * Platform Code: PDC
     * Module Code: 005
     * Quota Code: 11 - 20
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> update(Long id, ProductCategoryDTO request) {

        try {
            ProductCategory entity = repository.findById(id)
                    .orElseThrow(() -> {
                        log.error("PDC005W11 - Category with id {} not found", id);

                        return new RuntimeException("PDC005W11 - Category not found");
                    });

            entity.setCategory(request.getCategory());

            ProductCategory updated = repository.save(entity);

            return ResponseFactory.success(
                    "Category updated successfully",
                    HttpStatus.OK,
                    mapToResponse(updated)
            );
        } catch (Exception e) {
            log.error("PDC005E20 - Error updating category: {}", e.getMessage());

            return ResponseFactory.error(
                    "PDC005E20 - Failed to update category",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }

    }

    /**
     * Delete Product Category
     * Platform Code: PDC
     * Module Code: 005
     * Quota Code: 21 - 30
     */
    @Override
    public void delete(Long id) {
        try {
            ProductCategory entity = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("PDC005W21 - Category with id {} not found", id);
                        return new RuntimeException("PDC005W21 - Category not found");
                    });

            repository.delete(entity);
        } catch (Exception e) {
            log.error("PDC005E30 - Error deleting category: {}", e.getMessage());
            throw new RuntimeException("PDC005E30 - Failed to delete category");
        }
    }

    /**
     * Get One Product Category
     * Platform Code: PDC
     * Module Code: 005
     * Quota Code: 31 - 40
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> getOne(Long id) {

        try {
            ProductCategory entity = repository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("PDC005W31 - Category with id {} not found", id);

                        return new RuntimeException("PDC005W31 - Category not found");
                    });

            return ResponseFactory.success(
                    "Category fetched successfully",
                    HttpStatus.OK,
                    mapToResponse(entity)
            );
        } catch (Exception e) {
            log.error("PDC005E40 - Error fetching category: {}", e.getMessage());

            return ResponseFactory.error(
                    "PDC005E40 - Failed to fetch category",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }

    }

    /**
     * Get All Product Categories
     * Platform Code: PDC
     * Module Code: 005
     * Quota Code: 41 - 50
     */
    @Override
    public ResponseEntity<ApiResponse<Object>> getListProductCategory(
            String search,
            int page,
            int size,
            String sort,
            String direction,
            HttpServletRequest request
    ) {
        try {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    direction.equalsIgnoreCase("desc")
                            ? org.springframework.data.domain.Sort.by(sort).descending()
                            : org.springframework.data.domain.Sort.by(sort).ascending()
            );

            var categoryPage = repository.searchListProductCategory(search, pageable);

            List<ProductCategoryResponse> content = categoryPage.stream()
                    .map(this::mapToResponse)
                    .toList();

            int totalPages = categoryPage.getTotalPages();

            String selfUrl = PaginationUrlBuilderUtil.build(request, page);
            String nextUrl = PaginationUrlBuilderUtil.buildNullable(request, page + 1, totalPages);
            String prevUrl = PaginationUrlBuilderUtil.buildNullable(request, page - 1, totalPages);

            MetaDTO meta = new MetaDTO(
                    categoryPage.getNumber() + 1,
                    categoryPage.getSize(),
                    categoryPage.getTotalElements(),
                    categoryPage.getTotalPages()
            );

            LinksDTO links = new LinksDTO(
                    selfUrl,
                    nextUrl,
                    prevUrl
            );

            PageResponse<Object> response = new PageResponse<>(
                    content,
                    meta,
                    links
            );

            return ResponseFactory.success(
                    "Categories fetched successfully",
                    HttpStatus.OK,
                    response
            );

        } catch (Exception e) {
            log.error("PDC005E50 - Error fetching categories: {}", e.getMessage());

            return ResponseFactory.error(
                    "PDC005E50 - Failed to fetch categories",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
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




