package com.example.SmartShop.dto.response;

import com.example.SmartShop.enums.CustomerTier;
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
public class ClientProfileResponse {

    // Informations personnelles
    private Long id;
    private String nom;
    private String email;

    // Niveau de fidélité
    private CustomerTier niveauFidelite;

    // Statistiques
    private Integer nombreCommandes;
    private BigDecimal montantTotalDepense;
    private LocalDateTime premiereCommande;
    private LocalDateTime derniereCommande;

    // Avantages actuels
    private String avantagesFidelite;
    private BigDecimal prochainSeuilMontant;
    private Integer prochainSeuilCommandes;

    // Historique des commandes
    private List<OrderSummary> historiqueCommandes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderSummary {
        private Long id;
        private LocalDateTime date;
        private BigDecimal montantTotal;
        private String statut;
        private Integer nombreArticles;
    }
}
