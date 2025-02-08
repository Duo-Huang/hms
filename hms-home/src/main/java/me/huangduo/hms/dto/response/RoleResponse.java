package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.RoleTypeEnum;

import java.time.LocalDateTime;

public record RoleResponse(
        Integer roleId,
        RoleTypeEnum roleType,
        String roleName,
        String roleDescription,
        LocalDateTime createdAt
) {
}
