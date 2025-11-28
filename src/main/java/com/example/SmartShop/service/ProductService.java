package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.ProductRequest;
import com.example.SmartShop.dto.response.PaymentResponse;
import com.example.SmartShop.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
}
