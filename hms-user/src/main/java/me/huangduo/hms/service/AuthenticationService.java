package me.huangduo.hms.service;

import me.huangduo.hms.model.User;
import me.huangduo.hms.model.UserToken;
import me.huangduo.hms.exceptions.AuthenticationException;

public interface AuthenticationService {
    String generateToken(User user);

    boolean validateToken(UserToken userToken) throws AuthenticationException;

    UserToken parseToken(String token) throws AuthenticationException;

    boolean isTokenRevoked(UserToken userToken);

    void revokeToken(UserToken userToken);

}
