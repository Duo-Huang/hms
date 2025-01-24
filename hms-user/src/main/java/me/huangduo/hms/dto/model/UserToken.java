package me.huangduo.hms.dto.model;

import java.time.LocalDateTime;

public record UserToken(String jti,
                        Integer userId,
                        String subject,
                        String issuer,
                        LocalDateTime issuedAt,
                        LocalDateTime notBefore,
                        LocalDateTime expiration) {
}
