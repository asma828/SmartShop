package com.example.SmartShop.service.impl;

import com.example.SmartShop.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {
    @Value("${business.promo.discount:5}")
    private Integer promoDiscountRate;

    public final Pattern PROMO_PATTERN = Pattern.compile("^PROMO-[A-Z0-9]{4}$");

    @Override
    public boolean isValideCodePromo(String code) {
        if (code == null || code.isEmpty()) {
            return false;
        }
        boolean valid = PROMO_PATTERN.matcher(code).matches();
        return valid;
    }

    @Override
    public BigDecimal calculatePromoDiscount(String code, BigDecimal sousTotal) {
        if (!isValideCodePromo(code)) {
            return BigDecimal.ZERO;
        }

        BigDecimal promoDiscount = sousTotal.multiply(BigDecimal.valueOf(promoDiscountRate))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return promoDiscount;
    }

    @Override
    public Integer getPromoDiscountRate(String code){
        if (isValideCodePromo(code)){
            return 0;
        }
        return promoDiscountRate;
    }


}
