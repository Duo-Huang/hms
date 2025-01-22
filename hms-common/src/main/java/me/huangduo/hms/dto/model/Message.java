package me.huangduo.hms.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.huangduo.hms.enums.MessageType;

import java.time.LocalDateTime;

public record Message(
        Integer messageId,
        MessageType messageType,
        String messageContent,
        @JsonProperty("payload") String payload,
        LocalDateTime expiration,
        LocalDateTime createdAt
) {
    public Message {
        if (payload != null && !isValidJson(payload)) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
    }

    private static boolean isValidJson(String json) {
        try {
            new ObjectMapper().readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
