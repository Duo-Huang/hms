package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum MessageStatus implements SingleValueEnum {
    UNREAD(0),
    READ(1);


    private final int value;

    MessageStatus(int value) {
        this.value = value;
    }

    public static MessageStatus fromValue(int value) {
        for (MessageStatus messageStatus : MessageStatus.values()) {
            if (messageStatus.getValue() == value) {
                return messageStatus;
            }
        }
        throw new IllegalArgumentException("Invalid MessageStatus value " + value);
    }
}
