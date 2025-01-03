package me.huangduo.hms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.config.AppConfig;
import me.huangduo.hms.dao.RevokedUserTokensMapper;
import me.huangduo.hms.dao.entity.RevokedUserTokenEntity;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.exceptions.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final SecretKey key;

    private final AppConfig appConfig;

    private final RevokedUserTokensMapper revokedUserTokensMapper;

    private final ObjectMapper objectMapper;


    public AuthServiceImpl(AppConfig appConfig, RevokedUserTokensMapper revokedUserTokensMapper, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.key = Keys.hmacShaKeyFor(appConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        this.revokedUserTokensMapper = revokedUserTokensMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        String userJson = null;
        try {
            userJson = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            log.error("Failed to generate token due to JSON stringify error", e);
        }

        claims.put("userInfo", userJson);

        String jti = UUID.randomUUID().toString();

        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder().id(jti).claims(claims).subject(user.getUsername()).issuer(appConfig.getJwtIssuer()).issuedAt(now).notBefore(now).expiration(new Date(System.currentTimeMillis() + appConfig.getJwtTokenExpiredTime())).signWith(key).compact();
    }

    @Override
    public boolean validateToken(UserToken userToken) throws AuthenticationException {
        return userToken.issuedAt().isBefore(LocalDateTime.now()) && userToken.notBefore().isBefore(LocalDateTime.now()) && userToken.issuer().equals(appConfig.getJwtIssuer()) && userToken.expiration().isAfter(LocalDateTime.now());
    }

    @Override
    public UserToken parseToken(String token) {
        Claims claims = extractAllClaims(token);
        String userInfoJson = (String) claims.get("userInfo");
        User userInfo = null;
        try {
            userInfo = objectMapper.readValue(userInfoJson, User.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse token due to JSON parse error", e);
        }
        LocalDateTime issuedAt = claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime notBefore = claims.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expiration = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return new UserToken(claims.getId(), userInfo, claims.getSubject(), claims.getIssuer(), issuedAt, notBefore, expiration);
    }

    @Override
    public Boolean isTokenRevoked(UserToken userToken) {
        RevokedUserTokenEntity revokedJti = revokedUserTokensMapper.getByJti(userToken.jti());
        return Objects.nonNull(revokedJti);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).requireIssuer(appConfig.getJwtIssuer()).build().parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new AuthenticationException();
        }
    }

}
