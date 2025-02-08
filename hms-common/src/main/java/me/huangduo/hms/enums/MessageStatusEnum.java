package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum MessageStatusEnum implements SingleValueEnum {
    UNREAD(0),
    READ(1);


    private final int value;

    MessageStatusEnum(int value) {
        this.value = value;
    }

    public static MessageStatusEnum fromValue(int value) {
        for (MessageStatusEnum messageStatusEnum : MessageStatusEnum.values()) {
            if (messageStatusEnum.getValue() == value) {
                return messageStatusEnum;
            }
        }
        throw new IllegalArgumentException("Invalid MessageStatusEnum value " + value);
    }
}
