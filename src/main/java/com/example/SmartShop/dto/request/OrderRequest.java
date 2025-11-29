package com.example.SmartShop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;

    @NotEmpty(message = "La commande doit contenir au moins un produit")
    @Valid
    private List<OrderItemRequest> items;

    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Le code promo doit suivre le format PROMO-XXXX")
    private String codePromo;
}