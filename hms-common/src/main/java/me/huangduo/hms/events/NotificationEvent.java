package me.huangduo.hms.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.MessageType;

import java.time.LocalDateTime;

public class NotificationEvent extends HmsEvent<NotificationEvent.NotificationMessagePayload> {

    private final static LocalDateTime EXPIRATION = LocalDateTime.now().plusDays(7);

    public NotificationEvent(Object source, Integer homeId, User publisher, String message) {
        super(source, createMessage(homeId, publisher, message));
    }

    private static Message<NotificationMessagePayload> createMessage(Integer homeId, User publisher, String message) {
        NotificationMessagePayload payload = NotificationMessagePayload.builder().publisherUserId(publisher.getUserId()).build();

        return new Message<>(null, MessageType.NOTIFICATION, message, payload, EXPIRATION, homeId, LocalDateTime.now());
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuperBuilder
    public static class NotificationMessagePayload extends MessagePayload {

    }
}
