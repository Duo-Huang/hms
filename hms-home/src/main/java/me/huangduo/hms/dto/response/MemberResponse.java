package me.huangduo.hms.dto.response;

import me.huangduo.hms.dto.model.HomeRole;
import me.huangduo.hms.dto.model.SystemRole;

import java.time.LocalDateTime;

public record MemberResponse(
        Integer userId,
        String username,
        String memberName,
        RoleResponse role,
        LocalDateTime createdAt
) {
}
