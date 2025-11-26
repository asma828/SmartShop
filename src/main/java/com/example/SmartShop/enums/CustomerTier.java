package com.example.SmartShop.enums;

public enum CustomerTier {
    BASIC,      // Pas de remise
    SILVER,     // Remise 5% si commande >= 500 DH
    GOLD,       // Remise 10% si commande >= 800 DH
    PLATINUM    // Remise 15% si commande >= 1200 DH
}
