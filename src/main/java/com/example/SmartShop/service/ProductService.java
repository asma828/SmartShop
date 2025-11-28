package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.ProductRequest;
import com.example.SmartShop.dto.response.PaymentResponse;
import com.example.SmartShop.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse findProductById(Long id);
    List<ProductResponse> findAllProducts();
    ProductResponse updateProduct(Long id,ProductRequest request);
    ProductResponse deleteProduct(Long id);
}
