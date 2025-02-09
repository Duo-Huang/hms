package me.huangduo.hms.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.config.AppConfig;
import me.huangduo.hms.dao.RevokedUserTokensDao;
import me.huangduo.hms.dao.entity.RevokedUserTokenEntity;
import me.huangduo.hms.model.User;
import me.huangduo.hms.model.UserToken;
import me.huangduo.hms.exceptions.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final SecretKey key;

    private final AppConfig appConfig;

    private final RevokedUserTokensDao revokedUserTokensDao;


    public AuthenticationServiceImpl(AppConfig appConfig, RevokedUserTokensDao revokedUserTokensDao) {
        this.appConfig = appConfig;
        this.key = Keys.hmacShaKeyFor(appConfig.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        this.revokedUserTokensDao = revokedUserTokensDao;
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getUserId());

        String jti = UUID.randomUUID().toString();

        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .id(jti)
                .claims(claims)
                .subject(user.getUsername())
                .issuer(appConfig.getJwtIssuer())
                .issuedAt(now)
                .notBefore(now)
                .expiration(new Date(System.currentTimeMillis() + appConfig.getJwtTokenExpiredTime()))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(UserToken userToken) throws AuthenticationException {
        return userToken.issuedAt().isBefore(LocalDateTime.now()) && userToken.notBefore().isBefore(LocalDateTime.now()) && userToken.issuer().equals(appConfig.getJwtIssuer()) && userToken.expiration().isAfter(LocalDateTime.now());
    }

    @Override
    public UserToken parseToken(String token) {
        Claims claims = extractAllClaims(token);
        Integer userId = (Integer) claims.get("userId");

        LocalDateTime issuedAt = claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime notBefore = claims.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expiration = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return new UserToken(claims.getId(), userId, claims.getSubject(), claims.getIssuer(), issuedAt, notBefore, expiration);
    }

    @Override
    public Boolean isTokenRevoked(UserToken userToken) {
        RevokedUserTokenEntity revokedJti = revokedUserTokensDao.getByJti(userToken.jti());
        return Objects.nonNull(revokedJti);
    }

    @Override
    public void revokeToken(UserToken userToken) {
        RevokedUserTokenEntity revokedUserTokenEntity = RevokedUserTokenEntity.builder()
                .jti(userToken.jti())
                .expiration(userToken.expiration())
                .userId(userToken.userId())
                .build();
        revokedUserTokensDao.create(revokedUserTokenEntity);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).requireIssuer(appConfig.getJwtIssuer()).build().parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new AuthenticationException();
        }
    }

}
