package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.HmsRoleType;

import java.time.LocalDateTime;

public record RoleResponse(
        Integer roleId,
        HmsRoleType roleType,
        String roleName,
        String roleDescription,
        LocalDateTime createdAt
) {
}
