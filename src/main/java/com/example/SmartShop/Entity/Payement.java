package com.example.SmartShop.Entity;

import com.example.SmartShop.enums.PayementStatus;
import com.example.SmartShop.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payements")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Payement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    @ToString.Exclude
    private Order order;

    @Column(nullable = false)
    private Integer numeroPaiement;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private LocalDate datePaiement;

    @Column(nullable = false)
    private LocalDate dateEncaissement;

    @Column(nullable = false)
    private LocalDate dateEcheance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayementStatus payementStatus = PayementStatus.EN_ATTENTE;

    @Column
    private String reference;

    @Column
    private String banque;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;






}
