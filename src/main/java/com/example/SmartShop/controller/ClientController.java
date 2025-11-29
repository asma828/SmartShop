package com.example.SmartShop.controller;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.dto.request.ClientRequest;
import com.example.SmartShop.dto.response.ClientResponse;
import com.example.SmartShop.service.ClientService;
import com.example.SmartShop.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request){
        ClientResponse clientResponse = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientResponse);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ClientResponse> findClientById(@PathVariable Long id,HttpSession session){
        SessionUtil.getUser(session);
        ClientResponse response = clientService.getClientById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> findAllclients(){
        List<ClientResponse> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id,@RequestBody ClientRequest request){
        ClientResponse response = clientService.updateClient(id,request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
         clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
