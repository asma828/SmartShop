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

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id,HttpSession session){

        SessionUtil.getUser(session);
        OrderResponse response = orderService.findOrderById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(HttpSession session){
        SessionUtil.getUser(session);
        List<OrderResponse> response = orderService.findAllOrders();
        return ResponseEntity.ok(response);
    }


}
