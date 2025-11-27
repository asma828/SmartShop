package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.dto.request.ClientRequest;
import com.example.SmartShop.dto.response.ClientResponse;
import com.example.SmartShop.exception.BusinessRuleException;
import com.example.SmartShop.mapper.ClientMapper;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.service.ClientService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponse creatClient(ClientRequest request){
        if(clientRepository.existsByEmail(request.getEmail())){
            throw new BusinessRuleException("Un client avec ce email existe déjà");
            Client client = clientMapper.toEntity(request);
            Client saveClient = clientRepository.save(client);
            return clientMapper.toResponse(saveClient);
        }
    }
}
