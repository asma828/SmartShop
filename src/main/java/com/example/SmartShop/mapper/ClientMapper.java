package com.example.SmartShop.mapper;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.dto.request.ClientRequest;
import com.example.SmartShop.dto.response.ClientResponse;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toEntity(ClientRequest request) {
        if (request == null) {
            return null;
        }

        return Client.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .build();
    }

    public ClientResponse toResponse(Client client) {
        if (client == null) {
            return null;
        }

        return ClientResponse.builder()
                .id(client.getId())
                .nom(client.getNom())
                .email(client.getEmail())
                .niveauFidelite(client.getNiveauFidelite())
                .TotalOrders(client.getTotalOrders())
                .totalSpent(client.getTotalSpent())
                .firstOrderDate(client.getFirstOrderDate())
                .lastOrderDate(client.getLastOrderDate())
                .createdAt(client.getCreatedAt())
                .build();
    }

    public void updateEntityFromRequest(ClientRequest request, Client client) {
        if (request == null || client == null) {
            return;
        }

        client.setNom(request.getNom());
        client.setEmail(request.getEmail());
    }
}