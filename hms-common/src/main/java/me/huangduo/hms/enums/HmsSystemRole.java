package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum HmsSystemRole {
    HOME_ADMIN(HmsRoleType.SYSTEM_ROLE, "家庭管理员"),

    HOME_MEMBER(HmsRoleType.SYSTEM_ROLE, "家庭成员");

    private final HmsRoleType roleType;

    private final String roleName;

    HmsSystemRole(HmsRoleType roleType, String roleName) {
        this.roleType = roleType;
        this.roleName = roleName;
    }
}
