package me.huangduo.hms.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.huangduo.hms.dto.model.User;
import org.springframework.stereotype.Component;

@Component
public class InvitationEventFactory {
    private final ObjectMapper objectMapper;

    public InvitationEventFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public InvitationEvent createEvent(Object source, Integer homeId, String invitationCode, User inviter, User invitee) {
        return new InvitationEvent(objectMapper, source, homeId, invitationCode, inviter, invitee);
    }
}
