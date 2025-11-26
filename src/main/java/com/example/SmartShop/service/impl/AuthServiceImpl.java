package com.example.SmartShop.service.impl;

import com.example.SmartShop.dto.request.LoginRequest;
import com.example.SmartShop.repository.UserRepository;
import com.example.SmartShop.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;

@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthService login(LoginRequest request, HttpSession session){
        log.info("login attempt for username :{}",request.getUsername() );
    }
}
