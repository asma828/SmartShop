package com.example.SmartShop.repository;

import com.example.SmartShop.Entity.Payement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payement,Long> {
    // Compter les paiements d'une commande
    @Query("SELECT COUNT(p) FROM Payement p WHERE p.order.id = :orderId")
    Integer countPaymentsByOrder(Long orderId);

    List<Payement> findByOrderId(Long orderId);

}
