package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.OrderRequest;
import com.example.SmartShop.dto.response.OrderResponse;

public interface OrderService {
OrderResponse createOrder(OrderRequest request);

}
