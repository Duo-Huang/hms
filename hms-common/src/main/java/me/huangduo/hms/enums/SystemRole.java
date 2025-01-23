package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum SystemRole {
    HOME_ADMIN(RoleType.SYSTEM_ROLE, "家庭管理员"),

    HOME_MEMBER(RoleType.SYSTEM_ROLE, "家庭成员");

    private final RoleType roleType;

    private final String roleName;

    SystemRole(RoleType roleType, String roleName) {
        this.roleType = roleType;
        this.roleName = roleName;
    }
}
