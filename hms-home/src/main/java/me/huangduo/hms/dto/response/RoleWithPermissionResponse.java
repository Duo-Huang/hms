package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.HmsRoleType;

import java.time.LocalDateTime;
import java.util.List;

public record RoleWithPermissionResponse(
        Integer roleId,
        HmsRoleType roleType,
        String roleName,
        String roleDescription,
        List<PermissionResponse> permissions,
        LocalDateTime createdAt
) {
}
