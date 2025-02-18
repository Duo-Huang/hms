package me.huangduo.hms.config;

import me.huangduo.hms.interceptors.AuthenticationInterceptor;
import me.huangduo.hms.interceptors.AuthorizationInterceptor;
import me.huangduo.hms.interceptors.RequestLogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.stream.Stream;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestLogInterceptor requestLogInterceptor;
    private final AuthenticationInterceptor authInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;

    public WebConfig(RequestLogInterceptor requestLogInterceptor, AuthenticationInterceptor authInterceptor, AuthorizationInterceptor authorizationInterceptor) {
        this.requestLogInterceptor = requestLogInterceptor;
        this.authInterceptor = authInterceptor;
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://*:[5173,5500]", "https://*:[5173,5500]").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 是否允许带凭证
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludePaths = {"/error"}; // Spring will redirect to this url when exception occurs. we don't need to handle.
        registry.addInterceptor(requestLogInterceptor).addPathPatterns("/**").excludePathPatterns(excludePaths);
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns(concat(excludePaths, "/users/login", "/users/register"));
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**").excludePathPatterns(concat(excludePaths, "/users/**", "/homes", "/members/homes")); // allow /homes/**
    }

    private String[] concat(String[] array, String... additional) {
        return Stream.concat(Arrays.stream(array), Arrays.stream(additional))
                .toArray(String[]::new);
    }
}
