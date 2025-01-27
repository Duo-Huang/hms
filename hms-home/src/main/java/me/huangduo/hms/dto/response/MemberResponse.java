package me.huangduo.hms.dto.response;

import java.time.LocalDateTime;

public record MemberResponse(
        Integer userId,
        String memberName,
        RoleResponse role,
        LocalDateTime createdAt
) {
}
