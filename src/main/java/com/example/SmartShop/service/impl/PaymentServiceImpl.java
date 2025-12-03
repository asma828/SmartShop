package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.Order;
import com.example.SmartShop.Entity.Payement;
import com.example.SmartShop.dto.request.PaymentRequest;
import com.example.SmartShop.dto.response.PaymentResponse;
import com.example.SmartShop.enums.OrderStatus;
import com.example.SmartShop.enums.PayementStatus;
import com.example.SmartShop.enums.PaymentType;
import com.example.SmartShop.exception.BusinessRuleException;
import com.example.SmartShop.exception.ResourceNotFoundException;
import com.example.SmartShop.repository.OrderRepository;
import com.example.SmartShop.repository.PayementRepository;
import com.example.SmartShop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    public final PayementRepository payementRepository;
    public final OrderRepository orderRepository;

    @Value("${business.payment.cash.limit:20000}")
    private BigDecimal cashLimit;

    @Override
    public PaymentResponse addPayment(PaymentRequest request) {
            // 1. Vérifier la commande
            Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable avec l'ID:" + request.getOrderId()));

           // 2. Vérifier que la commande n'est pas déjà confirmée
            if (order.getStatus().name().equals("CONFIRMED")) {
                throw new BusinessRuleException("Commande introuvable avec l'ID:" + request.getOrderId());
        }

            // 3. Vérifier le montant
            if (request.getMontant().compareTo(order.getMontantRester()) > 0) {
                throw new BusinessRuleException("Le montant du paiement dépasse le montant restant dû");
            }

            // 4. Vérifier la limite légale pour les espèces
            if (request.getTypePaiement() == PaymentType.ESPECES && request.getMontant().compareTo(cashLimit) > 0) {
                throw new BusinessRuleException("Limite légale dépassée pour paiement en espèces (max " + cashLimit + " DH)");
            }

            // 5. Vérifier les champs obligatoires selon le type
            if(request.getTypePaiement() == PaymentType.CHEQUE && request.getReference() == null){
                throw new BusinessRuleException("La date d'échéance est obligatoire pour un chèque");
            }

            // 6. Calculer le numéro séquentiel
            Integer numeroPaiement = payementRepository.countPaymentsByOrder(request.getOrderId()+1);

            // 7. Créer le paiement
        Payement payement = Payement.builder()
                .order(order)
                .numeroPaiement(numeroPaiement)
                .montant(request.getMontant())
                .paymentType(request.getTypePaiement())
                .datePaiement(request.getDatePaiement())
                .dateEcheance(request.getDateEcheance())
                .payementStatus(PayementStatus.EN_ATTENTE)
                .reference(request.getReference())
                .banque(request.getBanque())
                .build();

        // 8. Si espèces ou virement immédiat, encaisser directement
        if (request.getTypePaiement() == PaymentType.ESPECES ||
                (request.getTypePaiement() == PaymentType.VIREMENT && request.getDateEcheance() == null)){
            payement.setPayementStatus(PayementStatus.ENCAISSE);
            payement.setDateEncaissement(LocalDate.now());
            // Mettre à jour le montant payé de la commande
            order.setMontantPayer(order.getMontantPayer().add(BigDecimal.valueOf(payement.getMontant())));
            order.setMontantRester(order.getMontantRester().subtract(BigDecimal.valueOf(payement.getMontant())));
        }
        Payement savedPayement = payementRepository.save(payement);
        orderRepository.save(order);
        return mapToResponse(savedPayement);

    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payement payement = payementRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Paiement introuvable avec l'ID: "+ id));
        return mapToResponse(payement);
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrder(Long orderId) {
        return payementRepository.findByOrderId(orderId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payement payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .numeroPaiement(payment.getNumeroPaiement())
                .montant(payment.getMontant())
                .typePaiement(payment.getPaymentType())
                .datePaiement(payment.getDatePaiement())
                .dateEncaissement(payment.getDateEncaissement())
                .dateEcheance(payment.getDateEcheance())
                .status(payment.getPayementStatus())
                .reference(payment.getReference())
                .banque(payment.getBanque())
                .createdAt(payment.getCreatedAt())
                .build();
    }



    }

