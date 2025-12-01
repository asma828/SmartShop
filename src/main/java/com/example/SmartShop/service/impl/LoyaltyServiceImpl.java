package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.enums.CustomerTier;
import com.example.SmartShop.service.LoyaltyService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;


@Service
public class LoyaltyServiceImpl implements LoyaltyService {

    // Seuils pour obtenir un niveau (basés sur l'historique)
    @Value("${business.loyalty.silver.orders:3}")
    private Integer silverOrdersThreshold;

    @Value("${business.loyalty.silver.amount:1000}")
    private Integer silverAmountThreshold;

    @Value("${business.loyalty.gold.orders:10}")
    private Integer goldOrdersThreshold;

    @Value("${business.loyalty.gold.amount:5000}")
    private Integer goldAmountThreshold;

    @Value("${business.loyalty.platinum.orders:20}")
    private Integer platinumOrdersThreshold;

    @Value("${business.loyalty.platinum.amount:15000}")
    private Integer platinumAmountThreshold;

    // Remises et seuils pour utiliser un niveau (sur futures commandes)
    @Value("${business.discount.silver.rate:5}")
    private Integer silverDiscountRate;

    @Value("${business.discount.silver.threshold:500}")
    private Integer silverMinimumPurchase;

    @Value("${business.discount.gold.rate:10}")
    private Integer goldDiscountRate;

    @Value("${business.discount.gold.threshold:800}")
    private Integer goldMinimumPurchase;

    @Value("${business.discount.platinum.rate:15}")
    private Integer platinumDiscountRate;

    @Value("${business.discount.platinum.threshold:1200}")
    private Integer platinumMinimumPurchase;

    @Override
    public CustomerTier calculeTier(Client client){

        Integer totalOrder = client.getTotalOrders();
        BigDecimal totalSpend = client.getTotalSpent();

        // PLATINUM : 20 commandes OU 15,000 DH
        if(totalOrder >=platinumOrdersThreshold ||
                totalSpend.compareTo(BigDecimal.valueOf(platinumAmountThreshold)) >= 0){
            return CustomerTier.PLATINUM;
        }

        // GOLD : 10 commandes OU 5,000 DH
        if(totalOrder >=goldOrdersThreshold ||
                totalSpend.compareTo(BigDecimal.valueOf(goldAmountThreshold)) >= 0){
            return CustomerTier.GOLD;
        }

        // SILVER : 3 commandes OU 1,000 DH
        if(totalOrder >=silverOrdersThreshold ||
                totalSpend.compareTo(BigDecimal.valueOf(silverAmountThreshold)) >=0){
            return CustomerTier.SILVER;
        }

        return CustomerTier.BASIC;

    }

    @Override
    public void updateClientTier(Client client){
        CustomerTier newTier = calculeTier(client);
        CustomerTier oldTier = client.getNiveauFidelite();

        if(newTier != oldTier){
            client.setNiveauFidelite(newTier);
        }
    }

    @Override
    public BigDecimal calculateDiscountRate(CustomerTier tier,BigDecimal sousTotal){
        if()
    }

    @Override
    public boolean isEligibleForDiscount(CustomerTier tier, BigDecimal sousTotal){
        switch (tier){
            case PLATINUM:
                return sousTotal.compareTo(BigDecimal.valueOf(platinumMinimumPurchase)) >=0;
            case GOLD:
                return sousTotal.compareTo(BigDecimal.valueOf(goldMinimumPurchase)) >=0;
            case SILVER:
                return sousTotal.compareTo(BigDecimal.valueOf(silverMinimumPurchase)) >=0;
            case BASIC:
            default:
                return false;
        }
    }

}
