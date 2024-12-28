package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.exceptions.AuthenticationException;

public interface AuthService {
    String generateToken(User user);

    boolean validateToken(String token) throws AuthenticationException;

    Integer extractUserId(String token);

    String extractUsername(String token);

}
