package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.Order;
import com.example.SmartShop.Entity.OrderItem;
import com.example.SmartShop.service.LoyaltyService;
import com.example.SmartShop.service.OrderCalculationService;
import com.example.SmartShop.service.PromoCodeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
                .reduce(BigDecimal.ONE,BigDecimal::add);
    }
}
