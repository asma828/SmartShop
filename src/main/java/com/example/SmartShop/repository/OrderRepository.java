package com.example.SmartShop.repository;

import com.example.SmartShop.Entity.Order;
import com.example.SmartShop.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByClientId(Long clientId);
    List<Order> findByStatus(OrderStatus status);
}
