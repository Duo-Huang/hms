package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.MessageType;
import me.huangduo.hms.events.HmsEvent;

import java.time.LocalDateTime;

public record MessageResponse(
        Integer messageId,
        MessageType messageType,
        String messageContent,
        HmsEvent.MessagePayload payload,
        LocalDateTime expiration,
        Integer homeId,
        LocalDateTime createdAt
) {
}
