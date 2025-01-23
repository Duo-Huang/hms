package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum RoleType implements SingleValueEnum {
    SYSTEM_ROLE(0),
    CUSTOM_ROLE(1);

    private final int value;

    RoleType(int value) {
        this.value = value;
    }

    public static RoleType fromValue(int value) {
        for (RoleType hmsRoleType : RoleType.values()) {
            if (hmsRoleType.getValue() == value) {
                return hmsRoleType;
            }
        }
        throw new IllegalArgumentException("Invalid RoleType value " + value);
    }
}
