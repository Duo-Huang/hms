package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum RoleTypeEnum implements SingleValueEnum {
    SYSTEM_ROLE(0),
    CUSTOM_ROLE(1);

    private final int value;

    RoleTypeEnum(int value) {
        this.value = value;
    }

    public static RoleTypeEnum fromValue(int value) {
        for (RoleTypeEnum roleTypeEnum : RoleTypeEnum.values()) {
            if (roleTypeEnum.getValue() == value) {
                return roleTypeEnum;
            }
        }
        throw new IllegalArgumentException("Invalid RoleTypeEnum value " + value);
    }
}
