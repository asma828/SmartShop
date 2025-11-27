package com.example.SmartShop.Entity;

import com.example.SmartShop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
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
    private BigDecimal remiseFidelite = BigDecimal.ZERO;

    @Column(nullable = false,precision = 10,scale = 2)
    @Builder.Default
    private BigDecimal remisePromo = BigDecimal.ZERO;

    //Total remise
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal montantRemiseTotal;

    //apres remis
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal montantHT;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal Tva;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal TotalTTC;

    @Column(nullable = false,precision = 10,scale = 2)
    @Builder.Default
    private BigDecimal montantPayer = BigDecimal.ZERO;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal montantRester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime updateAt;

    @Column
    private LocalDateTime confirmedAt;
}
