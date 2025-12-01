package com.example.SmartShop.service;

import java.math.BigDecimal;

public interface PromoCodeService {
    boolean isValideCodePromo(String code);
    BigDecimal calculatePromoDiscount(String code, BigDecimal sousTotal);
    Integer getPromoDiscountRate(String code);

}

}
