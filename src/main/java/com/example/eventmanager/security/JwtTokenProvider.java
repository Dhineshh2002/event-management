package com.example.eventmanager.security;

import com.example.eventmanager.cache.CacheService;
import com.example.eventmanager.common.enums.Role;
import com.example.eventmanager.exception.custom.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION_MS}")
    private long jwtExpirationInMs;

    private final CacheService cache;

    private SecretKey signingKey;

    @PostConstruct
    private void init() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET must be configured");
        }
        try {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

            if (keyBytes.length < 64) {
                MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
                keyBytes = sha512.digest(keyBytes);
            }
            signingKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception ex) {
            log.error("Failed to initialize JWT signing key", ex);
            throw new IllegalStateException("Failed to initialize JWT signing key", ex);
        }
    }

    public String generateToken(Long userId, String email, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        String userIdStr = Long.toString(userId);
        String jti = UUID.randomUUID().toString();

        String token = Jwts.builder()
                .setSubject(userIdStr)
                .claim("email", email)
                .claim("role", role.name())
                .setId(jti)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        cache.set(token, userIdStr, jwtExpirationInMs, TimeUnit.MILLISECONDS);
        return token;
    }

    public Map<String, Object> getUserFromToken(String token) {
        token = stripBearerPrefix(token);
        // ensure token is present in cache (revocation / server-side expiry)
        String userIdFromCache = cache.get(token, String.class);
        if (userIdFromCache == null) {
            log.info("Invalid or expired JWT token");
            throw new AuthenticationException("Invalid or expired JWT token");
        }

        Claims claims = validateToken(token);
        String subject = claims.getSubject();
        if (subject == null || subject.isBlank()) {
            log.info("Invalid JWT token: missing subject");
            throw new AuthenticationException("Invalid JWT token: missing subject");
        }

        return Map.of(
                "userId", Long.parseLong(subject),
                "role", claims.get("role", String.class)
        );
    }

    public Claims validateToken(String token) {
        if (token == null || token.isBlank()) {
            log.info("Missing JWT token");
            throw new AuthenticationException("Missing JWT token");
        }
        token = stripBearerPrefix(token);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            throw new AuthenticationException("Invalid or expired JWT token");
        }
    }

    private String stripBearerPrefix(String token) {
        String prefix = "Bearer ";
        if (token.startsWith(prefix)) {
            return token.substring(prefix.length()).trim();
        }
        return token;
    }
}
