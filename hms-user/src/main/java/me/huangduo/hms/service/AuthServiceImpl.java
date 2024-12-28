package me.huangduo.hms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.huangduo.hms.config.AppConfig;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.exceptions.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final SecretKey key;

    private final AppConfig appConfig;


    public AuthServiceImpl(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.key = Keys.hmacShaKeyFor(appConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());

        String jti = UUID.randomUUID().toString();

        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .id(jti)
                .claims(claims)
                .subject(user.getUsername())
                .issuer(appConfig.getJwtIssuer())
                .issuedAt(now)
                .notBefore(now) // 试试变短
                .expiration(new Date(System.currentTimeMillis() + appConfig.getJwtTokenExpiredTime()))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(String token) throws AuthenticationException {
        return !isTokenExpired(token);
    }

    @Override
    public Integer extractUserId(String token) {
        return (Integer) extractAllClaims(token).get("userId");
    }

    @Override
    public String extractUsername(String token) {
        return (String) extractAllClaims(token).get("username");
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer(appConfig.getJwtIssuer())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new AuthenticationException();
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
