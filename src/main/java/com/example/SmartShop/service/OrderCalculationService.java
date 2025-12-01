package com.example.SmartShop.service;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.Entity.Order;
import com.example.SmartShop.Entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface OrderCalculationService {
    BigDecimal calculateSousTotal(List<OrderItem> items);
    void calculateOrderAmounts(Order order, Client client);
    BigDecimal calculateTVA(BigDecimal montantHT);
}
