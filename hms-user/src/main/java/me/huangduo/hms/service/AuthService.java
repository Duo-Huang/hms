package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.exceptions.AuthenticationException;

public interface AuthService {
    String generateToken(User user);

    boolean validateToken(UserToken userToken) throws AuthenticationException;

    UserToken parseToken(String token) throws AuthenticationException;

    Boolean isTokenRevoked(UserToken userToken);

    void revokeToken(UserToken userToken);

}
