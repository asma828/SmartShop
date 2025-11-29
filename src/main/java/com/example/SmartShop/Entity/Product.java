package com.example.SmartShop.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal prix;

    @Column(nullable = false)
    @Builder.Default
    private Integer stock=0;

    // soft delete
    @Column(nullable = false ,updatable = false)
    private boolean deleted=false;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime updateAt;


    public boolean hasEnoughStock(Integer quantity) {
        return this.stock >= quantity;
    }

}
