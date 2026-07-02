package com.bhawanisingh.airesume.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class AuthResponse {

    private final String accessToken;

    private final String refreshToken;

    @Builder.Default
    private final String tokenType = "Bearer";

}