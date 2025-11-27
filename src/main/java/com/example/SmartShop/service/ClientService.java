package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.ClientRequest;
import com.example.SmartShop.dto.response.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(ClientRequest request);
    ClientResponse getClientById(Long id);
    List<ClientResponse> getAllClients();
    Page<ClientResponse> getAllClients(Pageable pageable);
    ClientResponse updateClient(Long id,ClientRequest request);
}
