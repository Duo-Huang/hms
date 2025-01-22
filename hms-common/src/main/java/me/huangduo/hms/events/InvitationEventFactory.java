package me.huangduo.hms.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class InvitationEventFactory {
    private final ObjectMapper objectMapper;

    public InvitationEventFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public InvitationEvent createEvent(Object source, String invitationCode, String inviterName, String inviteeName) {
        return new InvitationEvent(objectMapper, source, invitationCode, inviterName, inviteeName);
    }
}
