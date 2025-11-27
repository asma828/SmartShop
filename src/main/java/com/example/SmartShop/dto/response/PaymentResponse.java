package com.example.SmartShop.dto.response;

import com.example.SmartShop.enums.PayementStatus;
import com.example.SmartShop.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private Integer numeroPaiement;
    private BigDecimal montant;
    private PaymentType typePaiement;
    private LocalDate datePaiement;
    private LocalDate dateEncaissement;
    private LocalDate dateEcheance;
    private PayementStatus status;
    private String reference;
    private String banque;
    private LocalDateTime createdAt;
}