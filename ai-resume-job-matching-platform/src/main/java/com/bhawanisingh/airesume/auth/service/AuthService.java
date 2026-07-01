package com.bhawanisingh.airesume.auth.service;

import com.bhawanisingh.airesume.auth.dto.AuthResponse;
import com.bhawanisingh.airesume.auth.dto.LoginRequest;
import com.bhawanisingh.airesume.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}