package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum MessageType implements SingleValueEnum {
    NOTIFICATION(0),
    INVITATION(1);


    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public static MessageType fromValue(int value) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getValue() == value) {
                return messageType;
            }
        }
        throw new IllegalArgumentException("Invalid MessageType value " + value);
    }
}
