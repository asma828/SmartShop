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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public OrderResponse createOrder(OrderRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("client avec ce id n'existe pas"));

        Order order = Order.builder()
                .client(client)
                .codePromo(request.getCodePromo())
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal sousTotal = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findActiveById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
            if (!product.hasEnoughStock(itemRequest.getQuantity())) {
                order.setStatus(OrderStatus.REJECTED);
                orderRepository.save(order);
                throw new BusinessRuleException("Stock insuffisant pour le produit: " + product.getNom());
            }
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrix())
                    .build();
            orderItem.calculeTotal();
            orderItems.add(orderItem);
            sousTotal = sousTotal.add(orderItem.getTotalLine());
            product.decrementStock(itemRequest.getQuantity());
        }
        order.setOrderItems(orderItems);
        order.setSousTotal(sousTotal);

        order.setMontantRemiseTotal(BigDecimal.ZERO);
        order.setMontantHT(sousTotal);

        BigDecimal tva = sousTotal.multiply(BigDecimal.valueOf(TvaRate))
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);

        BigDecimal totalTTC = sousTotal.add(tva);
        order.setTotalTTC(totalTTC);
        order.setMontantRester(totalTTC);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    public OrderResponse findOrderById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Commande introuvable avec l'ID:"+ id));
        return mapToResponse(order);
    }
    @Override
    public List<OrderResponse> findAllOrders(){
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrderByClient(Long clientId){
        return orderRepository.findByClientId(clientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }

    @Override
    public List<OrderResponse> getOrderByStatus(OrderStatus status){
        return orderRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse mapToResponse(Order order){
        List<OrderItemResponse> orderItemList = order.getOrderItems().stream()
                .map(Item->OrderItemResponse.builder()
                        .id(Item.getId())
                        .productId(Item.getProduct().getId())
                        .productNom(Item.getProduct().getNom())
                        .quantity(Item.getQuantity())
                        .unitPrice(Item.getUnitPrice())
                        .totalLine(Item.getTotalLine())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .clientId(order.getClient().getId())
                .items(orderItemList)
                .codePromo(order.getCodePromo())
                .sousTotal(order.getSousTotal())
                .remiseFidelite(order.getRemiseFidelite())
                .remisePromo(order.getRemisePromo())
                .montantRemiseTotal(order.getMontantRemiseTotal())
                .montantHT(order.getMontantHT())
                .tva(order.getTva())
                .totalTTC(order.getTotalTTC())
                .montantPaye(order.getMontantPayer())
                .montantRestant(order.getMontantRester())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .confirmedAt(order.getConfirmedAt())
                .build();
    }
}
