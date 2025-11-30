package com.example.SmartShop.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id",nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // prix au moment de la commande
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal unitPrice;

    //Quantite * le prix unitaire
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal TotalLine;

    public void calculeTotal(){
        this.TotalLine = unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

}
