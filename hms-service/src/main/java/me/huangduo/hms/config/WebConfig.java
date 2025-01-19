package me.huangduo.hms.config;

import me.huangduo.hms.interceptors.AuthenticationInterceptor;
import me.huangduo.hms.interceptors.AuthorizationInterceptor;
import me.huangduo.hms.interceptors.RequestIdInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authInterceptor;

    private final RequestIdInterceptor requestIdInterceptor;

    private final AuthorizationInterceptor authorizationInterceptor;

    public WebConfig(AuthenticationInterceptor authInterceptor, RequestIdInterceptor requestIdInterceptor, AuthorizationInterceptor authorizationInterceptor) {
        this.authInterceptor = authInterceptor;
        this.requestIdInterceptor = requestIdInterceptor;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePaths = {"/error"}; // Spring will redirect to this url when exception occurs. we don't need to handle.
        registry.addInterceptor(requestIdInterceptor).addPathPatterns("/**").excludePathPatterns(excludePaths);
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns(concat(excludePaths, "/users/login", "/users/register"));
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**").excludePathPatterns(concat(excludePaths, "/users/**", "/homes", "/members/homes")); // allow /homes/**
    }

    private String[] concat(String[] array, String... additional) {
        return Stream.concat(Arrays.stream(array), Arrays.stream(additional))
                .toArray(String[]::new);
    }
}
