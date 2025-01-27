package me.huangduo.hms.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record HomeInfoResponse(
        Integer homeId,
        String homeName,
        String homeDescription,
        LocalDateTime createdAt
) {
}
