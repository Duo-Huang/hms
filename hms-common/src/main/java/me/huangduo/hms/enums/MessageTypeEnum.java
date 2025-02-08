package me.huangduo.hms.enums;

import lombok.Getter;

@Getter
public enum MessageTypeEnum implements SingleValueEnum {
    NOTIFICATION(0),
    INVITATION(1);


    private final int value;

    MessageTypeEnum(int value) {
        this.value = value;
    }

    public static MessageTypeEnum fromValue(int value) {
        for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()) {
            if (messageTypeEnum.getValue() == value) {
                return messageTypeEnum;
            }
        }
        throw new IllegalArgumentException("Invalid MessageTypeEnum value " + value);
    }
}
