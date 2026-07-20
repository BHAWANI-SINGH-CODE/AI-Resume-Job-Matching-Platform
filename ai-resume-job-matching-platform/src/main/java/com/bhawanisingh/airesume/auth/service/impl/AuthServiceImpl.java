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
import com.bhawanisingh.airesume.common.exception.DuplicateEmailException;
import com.bhawanisingh.airesume.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already registered");
        }

        // Dynamic Role Assignment: Frontend se aane wala role lo, warna default USER banao
        Role userRole = request.getRole() != null ? request.getRole() : Role.USER;

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole) // <--- UPDATED: Ab yeh dynamically set hoga
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

        Authentication authentication =
                authenticationManager.authenticate(

                        new UsernamePasswordAuthenticationToken(

                                request.getEmail(),

                                request.getPassword()

                        )

                );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken =
                jwtService.generateToken(user.getEmail());

        return AuthResponse

                .builder()

                .accessToken(accessToken)

                .refreshToken("")

                .build();

    }

}