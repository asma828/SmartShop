package com.example.SmartShop.repository;

import com.example.SmartShop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByUsername(String name);
    boolean existsByUsername(String name);

}
