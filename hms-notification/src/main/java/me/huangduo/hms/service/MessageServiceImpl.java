package me.huangduo.hms.service;

import me.huangduo.hms.dao.MessagesDao;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.events.EventHandler;
import me.huangduo.hms.events.InvitationEvent;
import me.huangduo.hms.events.NotificationEvent;
import me.huangduo.hms.mapper.MessageMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final SinksManager sinksManager = SinksManager.getInstance();

    private final MessagesDao messagesDao;

    private final MessageMapper messageMapper;

    public MessageServiceImpl(EventHandlerService eventHandlerService, MessagesDao messagesDao, MessageMapper messageMapper) {
        this.messagesDao = messagesDao;
        this.messageMapper = messageMapper;

        EventHandler genericHandler = event -> sinksManager.emit(event.getMessage());

        // register event handler
        eventHandlerService.registerEventHandler(InvitationEvent.class, genericHandler);
        eventHandlerService.registerEventHandler(NotificationEvent.class, genericHandler);
    }

    @Override
    public Flux<Message> getLiveMessage(Integer homeId) {

        return sinksManager.getFlux(); // TODO: filter
    }

    @Override
    public List<Message> getHistoryMessages(Integer homeId) {
        return messagesDao.getHistoryMessages(homeId).stream().map(messageMapper::toModel).collect(Collectors.toList());
    }
}
