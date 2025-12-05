package com.example.SmartShop.controller;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.enums.CustomerTier;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.service.LoyaltyService;
import com.example.SmartShop.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyService loyaltyService;
    private final ClientRepository clientRepository;

    /**
     * Recalculer le niveau de fidélité d'un client
     * PUT /api/loyalty/recalculate/{clientId}
     */
    @PutMapping("/recalculate/{clientId}")
    public ResponseEntity<Map<String, Object>> recalculateTier(
            @PathVariable Long clientId) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        CustomerTier oldTier = client.getNiveauFidelite();
        loyaltyService.updateClientTier(client);
        clientRepository.save(client);
        CustomerTier newTier = client.getNiveauFidelite();

        Map<String, Object> response = new HashMap<>();
        response.put("clientId", clientId);
        response.put("clientName", client.getNom());
        response.put("oldTier", oldTier);
        response.put("newTier", newTier);
        response.put("totalOrders", client.getTotalOrders());
        response.put("totalSpent", client.getTotalSpent());
        response.put("changed", oldTier != newTier);

        return ResponseEntity.ok(response);
    }

    /**
     * Calculer la remise applicable pour un montant donné
     * GET /api/loyalty/discount/{clientId}?amount=1000
     */
    @GetMapping("/discount/{clientId}")
    public ResponseEntity<Map<String, Object>> calculateDiscount(
            @PathVariable Long clientId,
            @RequestParam BigDecimal amount,
            HttpSession session) {
        SessionUtil.getUser(session);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        CustomerTier tier = client.getNiveauFidelite();
        boolean eligible = loyaltyService.isEligibleForDiscount(tier, amount);
        BigDecimal discountRate = loyaltyService.calculateDiscountRate(tier, amount);
        BigDecimal discountAmount = loyaltyService.calculateDiscountAmount(tier, amount);

        Map<String, Object> response = new HashMap<>();
        response.put("clientId", clientId);
        response.put("clientName", client.getNom());
        response.put("tier", tier);
        response.put("amount", amount);
        response.put("eligible", eligible);
        response.put("discountRate", discountRate + "%");
        response.put("discountAmount", discountAmount);
        response.put("finalAmount", amount.subtract(discountAmount));

        return ResponseEntity.ok(response);
    }
}