package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum SystemRoleEnum {
    HOME_ADMIN(RoleTypeEnum.SYSTEM_ROLE, "家庭管理员"),

    HOME_MEMBER(RoleTypeEnum.SYSTEM_ROLE, "家庭成员");

    private final RoleTypeEnum roleTypeEnum;

    private final String roleName;

    SystemRoleEnum(RoleTypeEnum roleTypeEnum, String roleName) {
        this.roleTypeEnum = roleTypeEnum;
        this.roleName = roleName;
    }
}
