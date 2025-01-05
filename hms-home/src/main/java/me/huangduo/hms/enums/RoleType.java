package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    SYSTEM_ROLE(0),
    CUSTOM_ROLE(1);

    private final int value;

    RoleType(int value) {
        this.value = value;
    }

    public static RoleType fromValue(int value) {
        for (RoleType roleType : RoleType.values()) {
            if (roleType.getValue() == value) {
                return roleType;
            }
        }
        throw new IllegalArgumentException("invalid RoleType value " + value);
    }
}
