package com.example.SmartShop.config;

import com.example.SmartShop.Entity.User;
import com.example.SmartShop.enums.UserRole;
import com.example.SmartShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
private final UserRepository userRepository;
private final BCryptPasswordEncoder bCryptPasswordEncoder;

@Override
    public void run(String... args) {
        createDefaultUsers();
    }

    private void createDefaultUsers(){
    if(!userRepository.existsByUsername("admin")){
        User admin = User.builder()
                .username("admin")
                .password(bCryptPasswordEncoder.encode("asma123"))
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin);
        log.info("default admin user created");
    }
    }
}
