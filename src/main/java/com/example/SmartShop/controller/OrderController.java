package com.example.SmartShop.controller;

import com.example.SmartShop.dto.request.OrderRequest;
import com.example.SmartShop.dto.response.OrderResponse;
import com.example.SmartShop.enums.OrderStatus;
import com.example.SmartShop.service.ClientService;
import com.example.SmartShop.service.OrderService;
import com.example.SmartShop.service.ProductService;
import com.example.SmartShop.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
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

    @GetMapping("client/{clientId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByClient(@PathVariable Long clientId){
        List<OrderResponse> responses = orderService.getOrderByClient(clientId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("status/{status}")
    public  ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderStatus status){
        List<OrderResponse> responses = orderService.getOrderByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable Long id){
        OrderResponse response = orderService.confirmOrder(id);
        return ResponseEntity.ok(response);
    }
}
