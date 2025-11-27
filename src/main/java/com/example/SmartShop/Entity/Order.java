package com.example.SmartShop.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id",nullable = false)
    private Client client;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @Builder.Default
    private List<Payement> payements = new ArrayList<>();

    @Column(length = 20)
    private String codePromo;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal sousTotal;

    @Column(nullable = false,precision = 10,scale = 2)
    @Builder.Default
    private BigDecimal remiseFidelite;

    @Column(nullable = false,precision = 10,scale = 2)
    @Builder.Default
    private BigDecimal remisePromo;


}
