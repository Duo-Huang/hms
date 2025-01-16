package me.huangduo.hms.dto.response;

import me.huangduo.hms.dto.model.HomeRole;

import java.time.LocalDateTime;

public record MemberResponse(
        Integer userId,
        String username,
        String memberName,
        HomeRole role,
        LocalDateTime createdAt
) {
}
