package me.huangduo.hms.dto.response;

import me.huangduo.hms.dto.model.Permission;
import me.huangduo.hms.enums.HmsRoleType;

import java.time.LocalDateTime;
import java.util.List;

public record RoleResponse(
        Integer roleId,
        HmsRoleType roleType,
        String roleName,
        String roleDescription,
        Integer homeId,
        List<PermissionResponse> permissions,
        LocalDateTime createdAt
) {
}
