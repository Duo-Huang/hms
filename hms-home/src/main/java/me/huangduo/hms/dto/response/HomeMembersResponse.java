package me.huangduo.hms.dto.response;

import java.time.LocalDateTime;

public record HomeMembersResponse(Integer userId, String memberName, Integer roleId, String roleName, String roleDescription, LocalDateTime createdAt) {
}
