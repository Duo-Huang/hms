package me.huangduo.hms.config;

import me.huangduo.hms.interceptors.AuthInterceptor;
import me.huangduo.hms.interceptors.RequestIdInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    private final RequestIdInterceptor requestIdInterceptor;

    public WebConfig(AuthInterceptor authInterceptor, RequestIdInterceptor requestIdInterceptor) {
        this.authInterceptor = authInterceptor;
        this.requestIdInterceptor = requestIdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestIdInterceptor).addPathPatterns("/**");
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/users/login", "/users/register");
    }
}
