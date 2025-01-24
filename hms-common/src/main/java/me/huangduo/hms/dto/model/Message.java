package me.huangduo.hms.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.huangduo.hms.enums.MessageType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Message<T> {
    private Integer messageId;

    private final MessageType messageType;

    private final String messageContent;

    private final T payload;

    private final LocalDateTime expiration;

    private final Integer homeId;

    private final LocalDateTime createdAt;
}
