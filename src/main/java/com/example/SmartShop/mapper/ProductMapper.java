package com.example.SmartShop.mapper;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.Entity.Product;
import com.example.SmartShop.dto.request.ClientRequest;
import com.example.SmartShop.dto.request.ProductRequest;
import com.example.SmartShop.dto.response.ProductResponse;

public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        return Product.builder()
                .nom(request.getNom())
                .prix(request.getPrix())
                .stock(request.getStock())
                .build();
    }

    public ProductResponse toResponse(Product product){
        if(product == null){
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .nom(product.getNom())
                .prix(product.getPrix())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdateAt())
                .build();
    }
}
