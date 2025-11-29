package com.example.SmartShop.dto.response;

import com.example.SmartShop.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long clientId;
    private String clientNom;
    private List<OrderItemResponse> items;
    private String codePromo;

    // Montants
    private BigDecimal sousTotal;           // Montant HT avant remises
    private BigDecimal remiseFidelite;
    private BigDecimal remisePromo;
    private BigDecimal montantRemiseTotal;
    private BigDecimal montantHT;           // Montant HT après remises
    private BigDecimal tva;
    private BigDecimal totalTTC;

    private BigDecimal montantPaye;
    private BigDecimal montantRestant;

    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
}