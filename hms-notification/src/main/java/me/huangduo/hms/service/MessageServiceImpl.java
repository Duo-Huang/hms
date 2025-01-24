package me.huangduo.hms.service;

import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.events.EventHandler;
import me.huangduo.hms.events.HmsEvent;
import me.huangduo.hms.events.InvitationEvent;
import me.huangduo.hms.events.NotificationEvent;
import me.huangduo.hms.mapper.InvitationMessageMapper;
import me.huangduo.hms.mapper.NotificationMessageMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final SinksManager sinksManager = SinksManager.getInstance();

    public MessageServiceImpl(EventHandlerService eventHandlerService, InvitationMessageMapper invitationMessageMapper, NotificationMessageMapper notificationMessageMapper) {

        EventHandler genericHandler = event -> sinksManager.emit(event.getMessage());

        // register event handler
        eventHandlerService.registerHandler(InvitationEvent.class, genericHandler);
        eventHandlerService.registerHandler(NotificationEvent.class, genericHandler);

        // register message mapper
        eventHandlerService.registerMessageMapper(InvitationEvent.class, invitationMessageMapper);
        eventHandlerService.registerMessageMapper(NotificationEvent.class, notificationMessageMapper);
    }

    @Override

    public Flux<Message<? extends HmsEvent.MessagePayload>> getLiveMessage() {
        return sinksManager.getFlux();
    }

    @Override
    public List<Message<? extends HmsEvent.MessagePayload>> getHistoryMessages(Integer homeId) {
        return null;
    }
}
