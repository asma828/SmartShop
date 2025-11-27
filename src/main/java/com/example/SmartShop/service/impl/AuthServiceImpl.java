package com.example.SmartShop.service.impl;

import com.example.SmartShop.Entity.User;
import com.example.SmartShop.dto.request.LoginRequest;
import com.example.SmartShop.dto.response.AuthResponse;
import com.example.SmartShop.exception.UnauthorizedException;
import com.example.SmartShop.repository.UserRepository;
import com.example.SmartShop.service.AuthService;
import com.example.SmartShop.util.SessionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AuthResponse login(LoginRequest request, HttpSession session){
        log.info("login attempt for username :{}",request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new UnauthorizedException("invalide username or password"));
        if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
            log.warn("Invalide password for username: {}",request.getUsername());
            throw new UnauthorizedException("Invalide username or password");
        }
        SessionUtil.setUser(session,user);

        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }

    @Override
    public void logout(HttpSession session){
        User user = SessionUtil.getUser(session);
        log.info("User {} logging out",user.getUsername());
        SessionUtil.clearUser(session);
    }

    @Override
    public AuthResponse getCurentUser(HttpSession session){
        User user = SessionUtil.getUser(session);

        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .message("current user retrieved")
                .build();
    }


}
