package com.example.SmartShop.mapper;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.Entity.Product;
import com.example.SmartShop.dto.request.ClientRequest;
import com.example.SmartShop.dto.request.ProductRequest;

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
}
