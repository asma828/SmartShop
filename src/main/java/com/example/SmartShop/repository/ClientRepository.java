package com.example.SmartShop.repository;

import com.example.SmartShop.Entity.Client;
import com.example.SmartShop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    Optional<Client> findByUser(User user);

}
