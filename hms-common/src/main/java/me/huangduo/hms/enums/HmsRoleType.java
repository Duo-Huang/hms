package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum HmsRoleType implements EnumValue {
    SYSTEM_ROLE(0),
    CUSTOM_ROLE(1);

    private final int value;

    HmsRoleType(int value) {
        this.value = value;
    }

    public static HmsRoleType fromValue(int value) {
        for (HmsRoleType hmsRoleType : HmsRoleType.values()) {
            if (hmsRoleType.getValue() == value) {
                return hmsRoleType;
            }
        }
        throw new IllegalArgumentException("Invalid RoleType value " + value);
    }
}
