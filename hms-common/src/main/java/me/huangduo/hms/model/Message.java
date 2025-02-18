package me.huangduo.hms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.huangduo.hms.enums.MessageTypeEnum;
import me.huangduo.hms.events.BroadcastEvent;
import me.huangduo.hms.events.HmsEvent;
import me.huangduo.hms.events.InvitationEvent;
import me.huangduo.hms.events.NotificationEvent;
import me.huangduo.hms.utils.JsonUtil;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class Message {

    private static final Map<MessageTypeEnum, Class<? extends HmsEvent.MessagePayload>> payloadDeserializerMap;
    private static final Map<MessageTypeEnum, Class<? extends HmsEvent.MessagePayloadResponse>> payloadResponseDeserializerMap;

    static {
        payloadDeserializerMap = Map.of(
                MessageTypeEnum.INVITATION, InvitationEvent.InvitationMessagePayload.class,
                MessageTypeEnum.NOTIFICATION, NotificationEvent.NotificationMessagePayload.class,
                MessageTypeEnum.BROADCAST, BroadcastEvent.BroadcastMessagePayload.class,
                MessageTypeEnum.HEARTBEAT, HmsEvent.MessagePayload.class
        );

        payloadResponseDeserializerMap = Map.of(
                MessageTypeEnum.INVITATION, InvitationEvent.InvitationMessagePayloadResponse.class,
                MessageTypeEnum.NOTIFICATION, NotificationEvent.NotificationMessagePayloadResponse.class,
                MessageTypeEnum.BROADCAST, BroadcastEvent.BroadcastMessagePayloadResponse.class,
                MessageTypeEnum.HEARTBEAT, HmsEvent.MessagePayloadResponse.class
        );
    }

    private Integer messageId;

    private final MessageTypeEnum messageType;

    private String messageContent;

    private final String payload;

    private final LocalDateTime expiration;

    private final LocalDateTime createdAt;

    public HmsEvent.MessagePayload getDeserializedPayload() {
        Class<? extends HmsEvent.MessagePayload> payloadClass = payloadDeserializerMap.get(messageType);
        if (payloadClass == null) {
            throw new IllegalArgumentException("No deserializer found for message type: " + messageType);
        }

        return JsonUtil.deserialize(payload, payloadClass);
    }

    public HmsEvent.MessagePayloadResponse getDeserializedPayloadResponse() {
        Class<? extends HmsEvent.MessagePayloadResponse> payloadClass = payloadResponseDeserializerMap.get(messageType);
        if (payloadClass == null) {
            throw new IllegalArgumentException("No deserializer found for message type: " + messageType);
        }

        return JsonUtil.deserialize(payload, payloadClass);
    }
}
