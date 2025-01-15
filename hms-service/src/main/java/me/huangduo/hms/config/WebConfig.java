package me.huangduo.hms.config;

import me.huangduo.hms.interceptors.AuthenticationInterceptor;
import me.huangduo.hms.interceptors.AuthorizationInterceptor;
import me.huangduo.hms.interceptors.RequestIdInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        registry.addInterceptor(requestIdInterceptor).addPathPatterns("/**");
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/users/login", "/users/register");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**").excludePathPatterns("/users/**", "/homes", "/members/homes"); // allow /homes/**
    }
}
