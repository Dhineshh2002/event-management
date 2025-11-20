package com.example.eventmanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION_MS}")
    private long jwtExpirationInMs;

    private final RedisTemplate<String, Object> redisTemplate;
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private Key getSigningKey() {
        return key;
    }

    public String generateToken(Long userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        String token = Jwts.builder()
                .setSubject(Long.toString(userId))
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
        redisTemplate.opsForValue().set(token, userId.toString(), jwtExpirationInMs, TimeUnit.MILLISECONDS);
        return token;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return Boolean.TRUE.equals(redisTemplate.hasKey(token));// This will prevent from NullPointerException
        } catch (Exception ex) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email", String.class);
    }

    public void revokeToken(String token) {
        redisTemplate.delete(token);
    }
}
