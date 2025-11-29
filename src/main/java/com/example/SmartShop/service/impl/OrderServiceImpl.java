package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.Entity.Order;
import com.example.SmartShop.Entity.OrderItem;
import com.example.SmartShop.Entity.Product;
import com.example.SmartShop.dto.request.OrderItemRequest;
import com.example.SmartShop.dto.request.OrderRequest;
import com.example.SmartShop.dto.response.OrderItemResponse;
import com.example.SmartShop.dto.response.OrderResponse;
import com.example.SmartShop.enums.OrderStatus;
import com.example.SmartShop.exception.BusinessRuleException;
import com.example.SmartShop.exception.ResourceNotFoundException;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.repository.OrderRepository;
import com.example.SmartShop.repository.ProductRepository;
import com.example.SmartShop.service.ClientService;
import com.example.SmartShop.service.OrderService;
import com.example.SmartShop.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    @Value("${business.tva.rate:20}")
    private Integer TvaRate;

    @Override
    public OrderResponse createOrder(OrderRequest request){
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(()->new ResourceNotFoundException("client avec ce id n'existe pas"));

        Order order = Order.builder()
                .client(client)
                .codePromo(request.getCodePromo())
                .status(OrderStatus.PENDING)
                .build();

 List<OrderItem> orderItems = new ArrayList<>();
 BigDecimal sousTotal = BigDecimal.ZERO;
 for(OrderItemRequest orderItem : request.getItems()){
     Product product = productRepository.findActiveById(orderItem.getProductId())
             .orElseThrow(()->new ResourceNotFoundException("Produit introuvable"));
     if(!product.hasEnoughStock(orderItem.getQuantity())){
         order.setStatus(OrderStatus.REJECTED);
         orderRepository.save(order);
         throw new BusinessRuleException("Stock insuffisant pour le produit: " + product.getNom());

     }
 }

    }
}
