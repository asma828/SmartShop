package com.example.SmartShop.controller;

import com.example.SmartShop.dto.request.PaymentRequest;
import com.example.SmartShop.dto.response.PaymentResponse;
import com.example.SmartShop.service.PaymentService;
import com.example.SmartShop.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPayementByOrder(@PathVariable Long orderId, HttpSession session){
        SessionUtil.getUser(session);
        List<PaymentResponse> response = paymentService.getPaymentsByOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/encaisser")
    public ResponseEntity<PaymentResponse> encaisserPayment(@PathVariable Long id){
        PaymentResponse response = paymentService.encaisserPayment(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<PaymentResponse> rejectPayment(@PathVariable Long id){
        PaymentResponse response = paymentService.rejectPayment(id);
        return ResponseEntity.ok(response);
    }
}
