package com.juaracoding.sikas.service.implement;

/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author afadi a.k.a. Paul Christian
Java Developer
Created on 11/23/2025 19:11
@Last Modified 11/23/2025 19:11
Version 1.0
*/

import com.juaracoding.sikas.dto.request.ProductRequest;
import com.juaracoding.sikas.dto.response.ProductCategoryResponse;
import com.juaracoding.sikas.dto.response.ProductResponse;
import com.juaracoding.sikas.model.Product;
import com.juaracoding.sikas.model.ProductCategory;
import com.juaracoding.sikas.repository.ProductCategoryRepository;
import com.juaracoding.sikas.repository.ProductRepository;
import com.juaracoding.sikas.service.ProductService;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    @Override
    public ProductResponse create(ProductRequest request) {

        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductCode(request.getProductCode());
        product.setPrice(request.getPrice());
        product.setBarcode(request.getBarcode());
        product.setStock(request.getStock());
        product.setCategory(category);
        product.setStatus(request.getStatus());
        product.setCreatedBy(1L);
        product.setUpdatedBy(1L);

        productRepository.save(product);

        return toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setProductName(request.getProductName());
        product.setBarcode(request.getBarcode());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);
        product.setStatus(request.getStatus());
        product.setUpdatedBy(1L);


        productRepository.save(product);

        return toResponse(product);
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public ProductResponse getOne(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .productName(p.getProductName())
                .productCode(p.getProductCode())
                .barcode(p.getBarcode())
                .price(p.getPrice())
                .status(p.getStatus())
                .stock(p.getStock())
                .createdDate(p.getCreatedDate())
                .updatedDate(p.getUpdatedDate())
                .createdBy(p.getCreatedBy())
                .updatedBy(p.getUpdatedBy())
                .category(
                        ProductCategoryResponse.builder()
                                .id(p.getCategory().getId())
                                .category(p.getCategory().getCategory())
                                .createdDate(p.getCategory().getCreatedDate())
                                .updatedDate(p.getCategory().getUpdatedDate())
                                .createdBy(p.getCategory().getCreatedBy())
                                .updatedBy(p.getCategory().getUpdatedBy())
                                .build()
                )
                .build();

}
}



