package me.huangduo.hms.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.enums.MessageTypeEnum;

import java.time.LocalDateTime;

@Slf4j
public class InvitationEvent extends HmsEvent {

    private final static LocalDateTime EXPIRATION = LocalDateTime.now().plusDays(3);

    public InvitationEvent(Object source, Integer homeId, User publisher, String invitationCode, User invitee) {
        super(source, createMessage(homeId, publisher, invitationCode, invitee));
    }

    private static Message createMessage(Integer homeId, User publisher, String invitationCode, User invitee) {
        String payload = InvitationMessagePayload.builder()
                .publisherUserId(publisher.getUserId())
                .inviteeUserId(invitee.getUserId())
                .invitationCode(invitationCode)
                .homeId(homeId)
                .build().serialize();
        String messageContent = publisher.getNickname() + " 邀请你加入他的家庭";

        return new Message(null, MessageTypeEnum.INVITATION, messageContent, payload, EXPIRATION, LocalDateTime.now());
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuperBuilder
    public static class InvitationMessagePayload extends MessagePayload {

        private final String invitationCode;

        private final Integer inviteeUserId;

        private final Integer homeId;

        @JsonCreator
        public InvitationMessagePayload(
                @JsonProperty("invitationCode") String invitationCode,
                @JsonProperty("inviteeUserId") Integer inviteeUserId,
                @JsonProperty("homeId") Integer homeId,
                @JsonProperty("publisherUserId") Integer publisherUserId) {
            super(publisherUserId);
            this.invitationCode = invitationCode;
            this.inviteeUserId = inviteeUserId;
            this.homeId = homeId;
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuperBuilder
    public static class InvitationMessagePayloadResponse extends MessagePayloadResponse {
        private final String invitationCode;

        @JsonCreator
        public InvitationMessagePayloadResponse(
                @JsonProperty("invitationCode") String invitationCode) {
            this.invitationCode = invitationCode;
        }
    }
}

