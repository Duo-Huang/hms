package me.huangduo.hms.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = UUID.randomUUID().toString();
        request.setAttribute("requestId", requestId);
        response.setHeader("X-Request-ID", requestId);

        String method = request.getMethod();
        String path = request.getRequestURI();

        MDC.put("requestId", requestId);
        MDC.put("method", method);
        MDC.put("path", path);
        MDC.put("startTime", String.valueOf(System.currentTimeMillis()));

        log.info("Request Started: requestId={}, method={}, path={}", requestId, method, path);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = Long.parseLong(MDC.get("startTime")); //TODO: Cannot parse null string
        long duration = System.currentTimeMillis() - startTime;
        log.info("Request completed: requestId={}, method={}, path={}, duration={} ms", MDC.get("requestId"), MDC.get("method"), MDC.get("path"), duration);
        MDC.clear();
    }
}
