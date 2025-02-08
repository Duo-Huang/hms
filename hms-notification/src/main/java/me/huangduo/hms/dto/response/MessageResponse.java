package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.MessageTypeEnum;
import me.huangduo.hms.events.HmsEvent;

import java.time.LocalDateTime;

public record MessageResponse(
        Integer messageId,
        MessageTypeEnum messageType,
        String messageContent,
        HmsEvent.MessagePayloadResponse payload,
        LocalDateTime expiration,
        LocalDateTime createdAt
) {
}
