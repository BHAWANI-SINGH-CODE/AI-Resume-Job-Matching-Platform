package com.bhawanisingh.airesume.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private final String accessToken;

    private final String refreshToken;

    @Builder.Default
    private final String tokenType = "Bearer";

}