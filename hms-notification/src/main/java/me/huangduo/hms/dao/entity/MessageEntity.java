package me.huangduo.hms.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.huangduo.hms.enums.MessageTypeEnum;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    private int messageId;

    private MessageTypeEnum messageType;

    private String messageContent;

    private String payload;

    private LocalDateTime expiration;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
