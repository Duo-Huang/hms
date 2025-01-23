package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.RoleType;

import java.time.LocalDateTime;

public record RoleResponse(
        Integer roleId,
        RoleType roleType,
        String roleName,
        String roleDescription,
        LocalDateTime createdAt
) {
}
