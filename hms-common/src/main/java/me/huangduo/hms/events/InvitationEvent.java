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

public class InvitationEvent extends HmsEvent {

    private final static LocalDateTime EXPIRATION = LocalDateTime.now().plusDays(3);

    public InvitationEvent(Object source, Integer homeId, User publisher, String invitationCode, User invitee) {
        super(source, createMessage(homeId, publisher, invitationCode, invitee));
    }

    private static Message createMessage(Integer homeId, User publisher, String invitationCode, User invitee) {
        String payload = InvitationMessagePayload.builder()
                .publisher(publisher)
                .receiver(invitee)
                .invitationCode(invitationCode)
                .homeId(homeId)
                .build()
                .serialize();

        String messageContent = """
                <#if currentUser.userId == %d>
                  你已邀请 %s 加入你的家庭<#t>
                <#elseif currentUser.userId == %d>
                  %s 邀请你加入他的家庭<#t>
                <#else>
                  %s 邀请 %s 加入你的家庭<#t>
                </#if>
                """
                // TODO: 是否只存userId在获取消息时填充以保持最新的user profile?
                .formatted(publisher.getUserId(), invitee.getNickname(), invitee.getUserId(), publisher.getNickname(), publisher.getNickname(), invitee.getNickname());


        return new Message(null, MessageTypeEnum.INVITATION, messageContent, payload, EXPIRATION, LocalDateTime.now());
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuperBuilder
    public static class InvitationMessagePayload extends MessagePayload {

        private final String invitationCode;

        private final User receiver;

        private final Integer homeId;

        @JsonCreator
        public InvitationMessagePayload(
                @JsonProperty("invitationCode") String invitationCode,
                @JsonProperty("receiver") User receiver,
                @JsonProperty("homeId") Integer homeId,
                @JsonProperty("publisher") User publisher) {
            super(publisher);
            this.invitationCode = invitationCode;
            this.receiver = receiver;
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

