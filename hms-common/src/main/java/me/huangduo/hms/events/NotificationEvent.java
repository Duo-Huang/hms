package me.huangduo.hms.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.MessageTypeEnum;

import java.time.LocalDateTime;

public class NotificationEvent extends HmsEvent {

    private final static LocalDateTime EXPIRATION = LocalDateTime.now().plusDays(7);

    public NotificationEvent(Object source, Integer homeId, User publisher, String message) {
        super(source, createMessage(homeId, publisher, message));
    }

    private static Message createMessage(Integer homeId, User publisher, String message) {
        String payload = NotificationMessagePayload.builder().publisherUserId(publisher.getUserId()).build().serialize();

        return new Message(null, MessageTypeEnum.NOTIFICATION, message, payload, EXPIRATION, LocalDateTime.now());
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuperBuilder
    public static class NotificationMessagePayload extends MessagePayload {

        @JsonCreator
        public NotificationMessagePayload(@JsonProperty("publisherUserId") Integer publisherUserId) {
            super(publisherUserId);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuperBuilder
    public static class NotificationMessagePayloadResponse extends MessagePayloadResponse {

        @JsonCreator
        public NotificationMessagePayloadResponse() {

        }
    }
}
