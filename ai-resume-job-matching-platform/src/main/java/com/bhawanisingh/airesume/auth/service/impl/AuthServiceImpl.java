package com.bhawanisingh.airesume.auth.service.impl;

import com.bhawanisingh.airesume.auth.dto.AuthResponse;
import com.bhawanisingh.airesume.auth.dto.LoginRequest;
import com.bhawanisingh.airesume.auth.dto.RegisterRequest;
import com.bhawanisingh.airesume.auth.entity.User;
import com.bhawanisingh.airesume.auth.enums.Role;
import com.bhawanisingh.airesume.auth.enums.UserStatus;
import com.bhawanisingh.airesume.auth.repository.UserRepository;
import com.bhawanisingh.airesume.auth.security.JwtService;
import com.bhawanisingh.airesume.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken("")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        throw new UnsupportedOperationException("Login will be implemented in Sprint 2 Step 8");

    }

}