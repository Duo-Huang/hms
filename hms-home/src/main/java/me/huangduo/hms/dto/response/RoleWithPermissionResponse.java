package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.RoleType;

import java.time.LocalDateTime;
import java.util.List;

public record RoleWithPermissionResponse(
        Integer roleId,
        RoleType roleType,
        String roleName,
        String roleDescription,
        List<PermissionResponse> permissions,
        LocalDateTime createdAt
) {
}
