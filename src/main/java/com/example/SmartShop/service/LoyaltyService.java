package com.example.SmartShop.service;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.enums.CustomerTier;

import java.math.BigDecimal;

public interface LoyaltyService {
    CustomerTier calculeTier(Client client);
    void updateClientTier(Client client);
    BigDecimal calculateDiscountRate(CustomerTier tier,BigDecimal sousTotal);
}
