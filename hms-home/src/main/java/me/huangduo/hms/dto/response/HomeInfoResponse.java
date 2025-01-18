package me.huangduo.hms.dto.response;

import java.time.LocalDateTime;

public record HomeInfoResponse(
        Integer homeId,
        String homeName,
        String homeDescription,
        LocalDateTime createdAt
) {
}
