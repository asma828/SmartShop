package com.example.SmartShop.controller;

import com.example.SmartShop.dto.request.PaymentRequest;
import com.example.SmartShop.dto.response.PaymentResponse;
import com.example.SmartShop.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payements")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> addPayement(@Valid @RequestBody PaymentRequest request){
        PaymentResponse response = paymentService.addPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaiementById(@PathVariable Long id){
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(response);
    }
}
