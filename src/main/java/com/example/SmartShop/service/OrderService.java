package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.OrderRequest;
import com.example.SmartShop.dto.response.OrderResponse;
import com.example.SmartShop.enums.OrderStatus;

public interface OrderService {
OrderResponse createOrder(OrderRequest request);
OrderResponse findOrderById(Long id);
OrderResponse findAllOrders();
OrderResponse getOrderByClient(Long clientId);
OrderResponse getOrderByStatus(OrderStatus status);
}
