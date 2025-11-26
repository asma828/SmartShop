package com.example.SmartShop.controller;

import com.example.SmartShop.dto.request.LoginRequest;
import com.example.SmartShop.dto.response.AuthResponse;
import com.example.SmartShop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session){
        AuthResponse response = authService.login(request,session);
        return ResponseEntity.ok(response);
    }
}
