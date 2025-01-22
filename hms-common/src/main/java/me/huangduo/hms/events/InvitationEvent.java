package me.huangduo.hms.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.enums.MessageType;

import java.time.LocalDateTime;

@Slf4j
public class InvitationEvent extends HmsEvent {

    private final static LocalDateTime EXPIRATION = LocalDateTime.now().plusDays(3);

    public InvitationEvent(ObjectMapper objectMapper, Object source, String invitationCode, String inviterName, String inviteeName) {
        super(source, createMessage(objectMapper, invitationCode, inviterName, inviteeName));
    }

    private static Message createMessage(ObjectMapper objectMapper, String invitationCode, String inviterName, String inviteeName) {
        InvitationMessagePayload payload = new InvitationMessagePayload(invitationCode, inviterName, inviteeName);

        String payloadJson = null;
        try {
            payloadJson = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.error("Failed to create event message due to JSON stringify error.", e);
        }
        String messageContent = inviterName + "邀请您加入他的家庭";

        return new Message(null, MessageType.INVITATION, messageContent, payloadJson, EXPIRATION, null);
    }

    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @Getter
    @AllArgsConstructor
    public static class InvitationMessagePayload extends MessagePayload {
        private final String invitationCode;

        private final String inviterName; // inviterName username

        private final String inviteeName; // inviteeName username

    }
}

