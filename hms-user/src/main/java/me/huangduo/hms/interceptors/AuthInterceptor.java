package me.huangduo.hms.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.AuthenticationException;
import me.huangduo.hms.service.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    /*
    * Token 校验
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws AuthenticationException {
        String authorizationHeader = request.getHeader("Authorization");

        response.setContentType("application/json");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            UserToken userToken = authService.parseToken(token);
            if (Objects.isNull(userToken.userInfo())) {
                log.error("Current userInfo in request is null");
            } else {
                System.out.println(userToken.userInfo().getCreatedAt());
            }
            if (!authService.isTokenRevoked(userToken) && authService.validateToken(userToken)) {
                request.setAttribute("userToken", userToken); // 包含User对象
                return true;
            }
        }
        throw new AuthenticationException(HmsErrorCodeEnum.USER_ERROR_101);
    }
}
