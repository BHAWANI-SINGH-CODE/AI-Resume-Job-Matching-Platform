package com.bhawanisingh.airesume.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email){

        Date now = new Date();

        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()

                .subject(email)

                .issuedAt(now)

                .expiration(expiry)

                .signWith(getSigningKey())

                .compact();

    }

    public String extractEmail(String token){

        Claims claims = Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

        return claims.getSubject();

    }

    public boolean isTokenExpired(String token){

        Claims claims = Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

        return claims.getExpiration().before(new Date());

    }

    public boolean validateToken(String token){

        return !isTokenExpired(token);

    }

}