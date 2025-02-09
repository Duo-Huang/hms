package me.huangduo.hms.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.enums.MessageTypeEnum;
import me.huangduo.hms.model.Message;
import me.huangduo.hms.model.User;

import java.time.LocalDateTime;

public class BroadcastEvent extends HmsEvent {

    private final static LocalDateTime EXPIRATION = LocalDateTime.now().plusDays(7);

    public BroadcastEvent(Object source, User publisher, String message) {
        super(source, createMessage(publisher, message));
    }

    private static Message createMessage(User publisher, String message) {
        String payload = BroadcastEvent.BroadcastMessagePayload.builder()
                .publisher(publisher)
                .build()
                .serialize();

        return new Message(null, MessageTypeEnum.BROADCAST, message, payload, EXPIRATION, LocalDateTime.now());
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuperBuilder
    public static class BroadcastMessagePayload extends MessagePayload {

        @JsonCreator
        public BroadcastMessagePayload(
                @JsonProperty("publisher") User publisher) {
            super(publisher);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuperBuilder
    public static class BroadcastMessagePayloadResponse extends MessagePayloadResponse {

        @JsonCreator
        public BroadcastMessagePayloadResponse() {

        }
    }
}
