package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.ClientRequest;
import com.example.SmartShop.dto.response.ClientProfileResponse;
import com.example.SmartShop.dto.response.ClientResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(ClientRequest request);
    ClientResponse getClientById(Long id);
    List<ClientResponse> getAllClients();
    ClientResponse updateClient(Long id,ClientRequest request);
    void deleteClient(Long id);
    ClientProfileResponse getClientProfile(HttpSession session);

}
