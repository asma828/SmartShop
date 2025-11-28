package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.Product;
import com.example.SmartShop.dto.request.ProductRequest;
import com.example.SmartShop.dto.response.ProductResponse;
import com.example.SmartShop.mapper.ProductMapper;
import com.example.SmartShop.repository.ProductRepository;
import com.example.SmartShop.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request){
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
         return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse findProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(("aucun produit trouver avec ce id"+id);
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> findAllProducts(){
       return  productRepository.findAll().stream()
               .map(productMapper::toResponse)
               .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long id,ProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(("produit avec ce id n'existe pas");
         productMapper.updateEntityFromRequest(request,product);
         Product updatedProduct = productRepository.save(product);
         return productMapper.toResponse(updatedProduct);
    }
}
