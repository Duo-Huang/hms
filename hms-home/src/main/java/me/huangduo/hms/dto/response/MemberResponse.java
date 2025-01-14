package me.huangduo.hms.dto.response;

import me.huangduo.hms.dto.model.Role;

import java.time.LocalDateTime;

public record MemberResponse(
        Integer userId,
        String username,
        String memberName,
        Role role,
        LocalDateTime createdAt
) {
}
