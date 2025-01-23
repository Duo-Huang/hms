package me.huangduo.hms.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.model.UserToken;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.exceptions.AccessDeniedException;
import me.huangduo.hms.exceptions.BusinessException;
import me.huangduo.hms.exceptions.RecordNotFoundException;
import me.huangduo.hms.service.CommonService;
import me.huangduo.hms.validators.IdValidator;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final CommonService commonService;

    private final IdValidator idValidator;

    public AuthorizationInterceptor(CommonService commonService, IdValidator idValidator) {
        this.commonService = commonService;
        this.idValidator = idValidator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        if (url.equals("/api/members") && Objects.equals(request.getMethod(), HttpMethod.POST.name())) { // ignore invite accept request
            return true;
        }

        UserToken userToken = (UserToken) request.getAttribute("userToken");
        User user = userToken.userInfo();

        Integer homeId = url.startsWith("/api/homes")
                ? getHomeIdFromPath(request)
                : getHomeIdFromHeader(request);

        MDC.put("homeId", String.valueOf(homeId));
        validateHomeAndUser(homeId, user.getUserId());
        request.setAttribute("homeId", homeId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
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
        return checkHomeId(pathVariables.get("homeId"), "homeId is invalid");
    }

    private Integer getHomeIdFromHeader(HttpServletRequest request) {
        String homeIdStr = Optional.ofNullable(request.getHeader("X-Home-ID"))
                .orElseThrow(() -> new IllegalArgumentException("Header X-Home-ID is required."));

        return checkHomeId(homeIdStr, "Header X-Home-ID is invalid.");
    }

    private Integer checkHomeId(String homeIdStr, String errorMessage) {
        if (idValidator.isValid(homeIdStr, null)) {
            return Integer.parseInt(homeIdStr);
        } else {
            log.error("homeId is invalid: homeId={}", homeIdStr);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateHomeAndUser(Integer homeId, Integer userId) {
        if (!(homeId > 0)) {
            RuntimeException ex = new IllegalArgumentException("homeId is invalid.");
            log.error("homeId is invalid: homeId={}", homeId, ex);
            throw ex;
        }
        checkHomeExisted(homeId);
        checkUserInHome(homeId, userId);
    }

    private void checkHomeExisted(Integer homeId) {
        if (commonService.getHomeById(homeId) == null) {
            BusinessException e = new RecordNotFoundException(HmsErrorCodeEnum.HOME_ERROR_203);
            log.error("The requested home does not exist", e);
            throw e;
        }
    }

    private void checkUserInHome(Integer homeId, Integer userId) {
        if (!commonService.isUserInHome(homeId, userId)) {
            BusinessException e = new AccessDeniedException();
            log.error("There is no such user in this home", e);
            throw e;
        }
    }
}
