package com.example.SmartShop.repository;

import com.example.SmartShop.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,String> {
    Optional<Client> findByEmail(String email);
    boolean existeByEmail(String email);
}
