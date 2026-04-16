package com.project.ordearly.security.auth.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.ordearly.security.auth.domain.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-access}")
    private Long accessTokenExpiration;

    @Value("${jwt.expiration-refresh}")
    private Long refreshTokenExpiration;

    public String generateToken(UserDetails userDetails) {
        var claims = new HashMap<String, Object>();

        claims.put("roles", userDetails.getAuthorities().stream()
                .map(role -> {
                    var auth = role.getAuthority();
                    return auth.startsWith("ROLE_") ? auth : "ROLE_" + auth;
                }).toList());

        claims.put("type", "access");

        if (userDetails instanceof CustomUserDetails customUserDetails)
            claims.put("userId", customUserDetails.getId());

        return buildToken(claims, userDetails, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        var claims = new HashMap<String, Object>();

        claims.put("type", "refresh");

        return buildToken(claims, userDetails, refreshTokenExpiration);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        var username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey() {
        var keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}