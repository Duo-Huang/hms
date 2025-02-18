package me.huangduo.hms.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.model.User;
import me.huangduo.hms.model.UserToken;
import me.huangduo.hms.exceptions.AuthenticationException;
import me.huangduo.hms.service.AuthenticationService;
import me.huangduo.hms.service.UserService;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
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
        if(Objects.equals(request.getMethod(), HttpMethod.OPTIONS.name())) {
            return true;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            UserToken userToken = authService.parseToken(token);
            Integer userId = userToken.userId();

            if (Objects.isNull(userId)) {
                log.error("Current userId in request is null.");
                throw new AuthenticationException();
            }

            User userInfo = null;

            if (Objects.isNull(userInfo = userService.getProfile(userId))) {
                log.error("Current user is not exists, userId={}", userId);
                throw new AuthenticationException();
            }

            if (!authService.isTokenRevoked(userToken) && authService.validateToken(userToken)) {
                request.setAttribute("userToken", userToken);
                request.setAttribute("userInfo", userInfo);
                MDC.put("userId", String.valueOf(userId));
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
