package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.PaymentRequest;
import com.example.SmartShop.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse addPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getPaymentsByOrder(Long orderId);
    PaymentResponse encaisserPayment(Long id);
    PaymentResponse rejectPayment(Long id);
}