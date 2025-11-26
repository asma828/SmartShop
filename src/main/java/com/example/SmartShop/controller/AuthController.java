package com.example.SmartShop.controller;

import com.example.SmartShop.dto.request.LoginRequest;
import com.example.SmartShop.dto.response.AuthResponse;
import com.example.SmartShop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

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

    @PostMapping("/logout")
    public ResponseEntity<Map<String,String>> logout(HttpSession session){
        authService.logout(session);
        return ResponseEntity.ok(Map.of("message","logout successefuly"));

    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurentUser(HttpSession session){
        AuthResponse response = authService.getCurentUser(session);
        return ResponseEntity.ok(response);
    }
}
