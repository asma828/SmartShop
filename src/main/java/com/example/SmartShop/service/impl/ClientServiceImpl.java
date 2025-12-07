package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.Entity.User;
import com.example.SmartShop.dto.request.ClientRequest;
import com.example.SmartShop.dto.response.ClientResponse;
import com.example.SmartShop.enums.UserRole;
import com.example.SmartShop.exception.BusinessRuleException;
import com.example.SmartShop.exception.ResourceNotFoundException;
import com.example.SmartShop.mapper.ClientMapper;
import com.example.SmartShop.repository.ClientRepository;
import com.example.SmartShop.repository.UserRepository;
import com.example.SmartShop.service.ClientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ClientResponse createClient(ClientRequest request){
        if(clientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Un client avec ce email existe déjà");
        }
        // Vérifier que le username n'existe pas déjà
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessRuleException("Ce username est déjà utilisé");
        }
        // 1. Créer le User avec role CLIENT
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.CLIENT)
                .build();
        User savedUser = userRepository.save(user);

            Client client = clientMapper.toEntity(request);
            client.setUser(savedUser);

            Client saveClient = clientRepository.save(client);
            return clientMapper.toResponse(saveClient);

    }

    @Override
    public ClientResponse getClientById(Long id){
    Client client = clientRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("client introvable par l'id :"+id));
    return clientMapper.toResponse(client);
    }

    @Override
    public List<ClientResponse> getAllClients(){
        return clientRepository.findAll()
                .stream().map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponse updateClient(Long id,ClientRequest request){
        Client client = clientRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("client introvable avec l'id" +id));

        if(client.getEmail().equals(request.getEmail()) && clientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Un client avec cet email existe déjà");
        }
            clientMapper.updateEntityFromRequest(request,client);
        // Optionnel: Mettre à jour le User si nécessaire
        if (client.getUser() != null) {
            // Vérifier username unique
            if (!client.getUser().getUsername().equals(request.getUsername()) &&
                    userRepository.existsByUsername(request.getUsername())) {
                throw new BusinessRuleException("Ce username est déjà utilisé");
            }

            client.getUser().setUsername(request.getUsername());

            // Mettre à jour le password seulement s'il est fourni et non vide
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                client.getUser().setPassword(passwordEncoder.encode(request.getPassword()));
            }
        }
            Client updatedClient = clientRepository.save(client);
            return clientMapper.toResponse(updatedClient);
    }

    @Override
    public void deleteClient(Long id){
        if(!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("client introvable avec l'id "  + id);
        }
         clientRepository.deleteById(id);
    }

}
