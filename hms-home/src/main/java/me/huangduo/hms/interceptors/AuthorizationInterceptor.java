package me.huangduo.hms.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.CommonDao;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.AccessDeniedException;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final CommonDao commonDao;

    public AuthorizationInterceptor(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        UserToken userToken = (UserToken) request.getAttribute("userToken");
        User user = userToken.userInfo();

        Integer homeId = url.startsWith("/homes")
                ? getHomeIdFromPath(request)
                : getHomeIdFromHeader(request);

        validateHomeAndUser(homeId, user.getUserId());

        request.setAttribute("homeId", homeId);
        return true;
    }

    @SuppressWarnings("unchecked")
    private Integer getHomeIdFromPath(HttpServletRequest request) throws NoHandlerFoundException {
        Map<String, String> pathVariables = Optional.ofNullable(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .orElseThrow(() -> {
                    HttpHeaders headers = new HttpHeaders();
                    Collections.list(request.getHeaderNames()).forEach(headerName -> headers.add(headerName, request.getHeader(headerName)));
                    return new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), headers);
                });

        return parseHomeId(pathVariables.get("homeId"), "homeId is invalid");
    }

    private Integer getHomeIdFromHeader(HttpServletRequest request) {
        String homeIdStr = Optional.ofNullable(request.getHeader("X-Home-ID"))
                .orElseThrow(() -> new IllegalArgumentException("Header X-Home-ID is required."));

        return parseHomeId(homeIdStr, "Header X-Home-ID is invalid.");
    }

    private Integer parseHomeId(String homeIdStr, String errorMessage) {
        try {
            return Integer.parseInt(homeIdStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateHomeAndUser(Integer homeId, Integer userId) {
        checkHomeExisted(homeId);
        checkUserInHome(homeId, userId);
    }

    private void checkHomeExisted(Integer homeId) {
        if (commonDao.getHomeById(homeId) == null) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("The requested home does not exist: homeId={}", homeId, e);
            throw e;
        }
    }

    private void checkUserInHome(Integer homeId, Integer userId) {
        if (commonDao.isUserInHome(homeId, userId) == 0) {
            throw new AccessDeniedException(HmsErrorCodeEnum.HOME_ERROR_2016);
        }
    }
}
