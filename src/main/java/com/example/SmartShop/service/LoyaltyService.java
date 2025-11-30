package com.example.SmartShop.service;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.enums.CustomerTier;

public interface LoyaltyService {
    CustomerTier calculeTier(Client client);
    void updateClientTier(Client client);
}
