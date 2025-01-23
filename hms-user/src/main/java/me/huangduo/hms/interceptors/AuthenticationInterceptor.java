package me.huangduo.hms.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.AuthenticationException;
import me.huangduo.hms.service.AuthenticationService;
import me.huangduo.hms.service.UserService;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationService authService;

    private final UserService userService;

    public AuthenticationInterceptor(AuthenticationService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /*
     * Token 校验
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AuthenticationException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            UserToken userToken = authService.parseToken(token);
            User user = userToken.userInfo();
            if (Objects.isNull(user)) {
                log.error("Current userInfo in request is null.");
                throw new AuthenticationException();
            }

            if (Objects.isNull(userService.getProfile(user.getUserId()))) {
                log.error("Current user is not exists, userId={}", user.getUserId());
                throw new AuthenticationException();
            }

            if (!authService.isTokenRevoked(userToken) && authService.validateToken(userToken)) {
                request.setAttribute("userToken", userToken); // 包含User对象
                MDC.put("userId", String.valueOf(userToken.userInfo().getUserId()));
                return true;
            }
        }
        log.error("Current user authentication failed.");
        throw new AuthenticationException();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
    }
}
