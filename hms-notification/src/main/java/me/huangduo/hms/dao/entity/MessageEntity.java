package me.huangduo.hms.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.enums.MessageType;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
public class MessageEntity extends BaseEntity {
    private int messageId;

    private MessageType messageType;

    private String messageContent;

    @JsonProperty("payload")
    private String payload;

    private LocalDateTime expiration;

    public void setPayload(String payload) {
        if (payload != null && !isValidJson(payload)) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
        this.payload = payload;
    }

    private boolean isValidJson(String json) {
        try {
            new ObjectMapper().readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
