package me.huangduo.hms.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.huangduo.hms.enums.MessageType;
import me.huangduo.hms.events.HmsEvent;
import me.huangduo.hms.events.InvitationEvent;
import me.huangduo.hms.events.NotificationEvent;
import me.huangduo.hms.utils.JsonUtil;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class Message {

    private static final Map<MessageType, Class<? extends HmsEvent.MessagePayload>> deserializerMap;

    static {
        deserializerMap = Map.of(
                MessageType.INVITATION, InvitationEvent.InvitationMessagePayload.class,
                MessageType.NOTIFICATION, NotificationEvent.NotificationMessagePayload.class
        );
    }

    private Integer messageId;

    private final MessageType messageType;

    private final String messageContent;

    private final String payload;

    private final LocalDateTime expiration;

    private final Integer homeId;

    private final LocalDateTime createdAt;

    public HmsEvent.MessagePayload getDeserializedPayload() {
        Class<? extends HmsEvent.MessagePayload> payloadClass = deserializerMap.get(messageType);
        if (payloadClass == null) {
            throw new IllegalArgumentException("No deserializer found for message type: " + messageType);
        }

        return JsonUtil.deserialize(payload, payloadClass);
    }
}
