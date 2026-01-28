package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.Entity.Order;
import com.example.SmartShop.Entity.OrderItem;
import com.example.SmartShop.enums.CustomerTier;
import com.example.SmartShop.service.LoyaltyService;
import com.example.SmartShop.service.OrderCalculationService;
import com.example.SmartShop.service.PromoCodeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCalculationServiceImpl implements OrderCalculationService {

    private final LoyaltyService loyaltyService;
    private final PromoCodeService promoCodeService;

    @Value("${business.tva.rate:20}")
    private Integer tvaRate;

    @Override
    public BigDecimal calculateSousTotal(List<OrderItem> items){
        return items.stream().map(OrderItem::getTotalLine)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public void calculateOrderAmounts(Order order, Client client) {
        // 1. Calculer le sous-total (HT avant remises)
        BigDecimal soustotal = calculateSousTotal(order.getOrderItems());
        order.setSousTotal(soustotal);
        // 2. Calculer la remise fidélité
        BigDecimal remiseFidelite = loyaltyService
                .calculateDiscountAmount(client.getNiveauFidelite(),soustotal);
        order.setRemiseFidelite(remiseFidelite);
        // 3. Calculer la remise promo
        BigDecimal remisePromo = BigDecimal.ZERO;
        if (order.getCodePromo() != null && !order.getCodePromo().isEmpty()) {
             remisePromo = promoCodeService.calculatePromoDiscount(order.getCodePromo(), soustotal);
            order.setRemisePromo(remisePromo);
        }else {
            order.setRemisePromo(BigDecimal.ZERO);
        }
        // 4. Calculer le montant total des remises
        BigDecimal montantRemiseTotal = remiseFidelite.add(remisePromo);
        order.setMontantRemiseTotal(montantRemiseTotal);

        // 5. Calculer le montant HT après remises
        BigDecimal montantHT = soustotal.subtract(montantRemiseTotal);
        order.setMontantHT(montantHT);
        // 6. Calculer la TVA (sur le montant APRÈS remise)
        BigDecimal montantTVA = montantHT.multiply(BigDecimal.valueOf(tvaRate))
                .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
        order.setTva(montantTVA);
        // 7. Calculer le total TTC
        BigDecimal totalTTC = montantHT.add(montantTVA);
        order.setTotalTTC(totalTTC);
        // 8. Initialiser le montant restant
        if(order.getMontantPayer() == null){
            order.setMontantPayer(BigDecimal.ZERO);
        }
        order.setMontantRester(totalTTC.subtract(order.getMontantPayer()));

    }

    }
