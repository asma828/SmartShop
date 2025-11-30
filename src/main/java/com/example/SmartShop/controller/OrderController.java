package com.example.SmartShop.controller;

import com.example.SmartShop.dto.request.OrderRequest;
import com.example.SmartShop.dto.response.OrderResponse;
import com.example.SmartShop.service.ClientService;
import com.example.SmartShop.service.OrderService;
import com.example.SmartShop.service.ProductService;
import com.example.SmartShop.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request, HttpSession session){
       OrderResponse response = orderService.createOrder(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
