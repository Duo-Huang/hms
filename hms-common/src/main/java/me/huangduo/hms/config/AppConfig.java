package me.huangduo.hms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    private String jwtIssuer;

    private String jwtSecret;

    private long jwtTokenExpiredTime;

    private int sseHeartbeatIntervalOfSec;
}
