package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.User;
import com.example.SmartShop.dto.request.LoginRequest;
import com.example.SmartShop.dto.response.AuthResponse;
import com.example.SmartShop.exception.UnauthorizedException;
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
    public AuthResponse login(LoginRequest request, HttpSession session){
        log.info("login attempt for username :{}",request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new UnauthorizedException("invalide username or password"));
        if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
            log.warn("Invalide password for username: {}",request.getUsername());
            throw new UnauthorizedException("Invalide username or password");
        }
    }

}
