package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.*;
import com.example.SmartShop.dto.request.OrderItemRequest;
import com.example.SmartShop.dto.request.OrderRequest;
import com.example.SmartShop.dto.response.OrderItemResponse;
import com.example.SmartShop.dto.response.OrderResponse;
import com.example.SmartShop.enums.OrderStatus;
import com.example.SmartShop.exception.BusinessRuleException;
import com.example.SmartShop.exception.ResourceNotFoundException;
import com.example.SmartShop.repository.*;
import com.example.SmartShop.service.LoyaltyService;
import com.example.SmartShop.service.OrderCalculationService;
import com.example.SmartShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderCalculationService calculationService;
    private final LoyaltyService loyaltyService;

    @Override
    public OrderResponse createOrder(OrderRequest request) {

        // 1. Vérifier le client
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'ID: " + request.getClientId()));

        // 2. Créer la commande
        Order order = Order.builder()
                .client(client)
                .codePromo(request.getCodePromo())
                .status(OrderStatus.PENDING)
                .build();

        // 3. Créer les items et vérifier le stock
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findActiveById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable avec l'ID: " + itemRequest.getProductId()));

            // Vérifier le stock AVANT de créer la commande
            if (!product.hasEnoughStock(itemRequest.getQuantity())) {

                // Sauvegarder la commande avec statut REJECTED
                order.setStatus(OrderStatus.REJECTED);
                order.setSousTotal(BigDecimal.ZERO);
                order.setMontantHT(BigDecimal.ZERO);
                order.setTva(BigDecimal.ZERO);
                order.setTotalTTC(BigDecimal.ZERO);
                order.setMontantRester(BigDecimal.ZERO);
                order.setRemiseFidelite(BigDecimal.ZERO);
                order.setRemisePromo(BigDecimal.ZERO);
                order.setMontantRemiseTotal(BigDecimal.ZERO);
                orderRepository.save(order);

                throw new BusinessRuleException("Stock insuffisant pour le produit: " + product.getNom() +
                        " (Demandé: " + itemRequest.getQuantity() + ", Disponible: " + product.getStock() + ")");
            }

            // Créer l'item avec le prix actuel du produit
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(product.getPrix())
                    .build();

            orderItem.calculeTotal();
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        // 4. Calculer tous les montants (sous-total, remises, TVA, total)
        calculationService.calculateOrderAmounts(order, client);

        // 6. Sauvegarder la commande
        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    public OrderResponse findOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable avec l'ID: " + id));

        return mapToResponse(order);
    }

    @Override
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrderByClient(Long clientId) {
        return orderRepository.findByClientId(clientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrderByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse confirmOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable avec l'ID: " + id));

        // Vérifications
        if (!order.canBeConfirmed()) {
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new BusinessRuleException("Seules les commandes PENDING peuvent être confirmées");
            }
            if (!order.isFullyPaid()) {
                throw new BusinessRuleException("La commande doit être totalement payée avant confirmation (Restant: " +
                        order.getMontantRester() + " DH)");
            }
        }

        // Vérifier à nouveau le stock avant confirmation (au cas où il aurait changé)
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            if (!product.hasEnoughStock(item.getQuantity())) {
                throw new BusinessRuleException("Stock insuffisant pour confirmer la commande. Produit: " +
                        product.getNom() + " (Demandé: " + item.getQuantity() + ", Disponible: " + product.getStock() + ")");
            }
        }

        // DÉCRÉMENTER LE STOCK (lors de la confirmation)
        for (OrderItem item : order.getOrderItems()) {
            item.getProduct().decrementStock(item.getQuantity());
        }

        // Confirmer la commande
        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());

        // Mettre à jour les statistiques du client
        Client client = order.getClient();
        client.incrementOrder();
        client.addToTotalSpent(order.getTotalTTC());
        client.updateOrderDates(order.getCreatedAt());

        // Recalculer le niveau de fidélité du client
        loyaltyService.updateClientTier(client);

        // Sauvegarder
        clientRepository.save(client);
        orderRepository.save(order);

        return mapToResponse(order);
    }

    @Override
    public OrderResponse canclOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable avec l'ID: " + id));

        if (!order.canBeCanceled()) {
            throw new BusinessRuleException("Seules les commandes PENDING peuvent être annulées");
        }

        // Annuler la commande
        order.setStatus(OrderStatus.CANCELED);

        orderRepository.save(order);

        return mapToResponse(order);
    }

    // Méthode utilitaire pour mapper Order vers OrderResponse

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productNom(item.getProduct().getNom())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalLine(item.getTotalLine())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .clientId(order.getClient().getId())
                .clientNom(order.getClient().getNom())
                .items(items)
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