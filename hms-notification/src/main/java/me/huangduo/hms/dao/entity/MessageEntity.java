package me.huangduo.hms.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.huangduo.hms.enums.MessageType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    private int messageId;

    private MessageType messageType;

    private String messageContent;

    private String payload;

    private LocalDateTime expiration;

    private Integer homeId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
