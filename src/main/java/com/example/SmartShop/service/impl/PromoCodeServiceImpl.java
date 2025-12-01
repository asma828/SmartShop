package com.example.SmartShop.service.impl;

import com.example.SmartShop.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {
    @Value("${business.promo.discount:5}")
    private Integer promoDiscountRate;

    public final Pattern PROMO_PATTERN = Pattern.compile("^PROMO-[A-Z0-9]{4}$");

    @Override
    public boolean isValideCodePromo(String code){
        if(code == null || code.isEmpty()){
              return false;
        }
        boolean valid = PROMO_PATTERN.matcher(code).matches();
        return valid;
    }
}
