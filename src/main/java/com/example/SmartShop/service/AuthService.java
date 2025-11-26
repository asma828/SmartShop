package com.example.SmartShop.service;

import com.example.SmartShop.dto.request.LoginRequest;
import com.example.SmartShop.dto.response.AuthResponse;

import javax.servlet.http.HttpSession;

public interface AuthService {
    AuthResponse login(LoginRequest request, HttpSession session);
    void logout(HttpSession session);
}
