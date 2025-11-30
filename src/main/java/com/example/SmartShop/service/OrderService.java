package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.OrderRequest;
import com.example.SmartShop.dto.response.OrderResponse;
import com.example.SmartShop.enums.OrderStatus;

import java.util.List;

public interface OrderService {
  OrderResponse createOrder(OrderRequest request);
  OrderResponse findOrderById(Long id);
  List<OrderResponse> findAllOrders();
  List<OrderResponse> getOrderByClient(Long clientId);
  List<OrderResponse> getOrderByStatus(OrderStatus status);
  OrderResponse confirmOrder(Long id);
  OrderResponse canclOrder(Long id);

}
