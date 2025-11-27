package com.example.SmartShop.dto.request;

import com.example.SmartShop.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "L'ID de la commande est obligatoire")
    private Long orderId;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    private BigDecimal montant;

    @NotNull(message = "Le type de paiement est obligatoire")
    private PaymentType typePaiement;

    @NotNull(message = "La date de paiement est obligatoire")
    private LocalDate datePaiement;

    private LocalDate dateEcheance;  // Obligatoire pour CHEQUE

    private String reference;  // Numéro de reçu, chèque, virement

    private String banque;  // Pour CHEQUE et VIREMENT
}