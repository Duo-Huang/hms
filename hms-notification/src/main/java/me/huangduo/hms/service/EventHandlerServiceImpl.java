package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.MessagesDao;
import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.events.EventHandler;
import me.huangduo.hms.events.HmsEvent;
import me.huangduo.hms.mapper.MessageMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EventHandlerServiceImpl implements EventHandlerService {
    private final Map<Class<? extends HmsEvent>, EventHandler> handlers = new HashMap<>();

    private final MessagesDao messagesDao;

    private final MessageMapper messageMapper;

    public EventHandlerServiceImpl(MessagesDao messagesDao, MessageMapper messageMapper) {
        this.messagesDao = messagesDao;
        this.messageMapper = messageMapper;
    }

    @Override
    public void registerEventHandler(Class<? extends HmsEvent> eventType, EventHandler handler) {
        log.debug("Registering handler for event type: {}", eventType);
        handlers.put(eventType, handler);
    }


    @Override
    @EventListener
    public void handleEvent(HmsEvent event) {
        log.info("Received event: {}", event);

        MessageEntity messageEntity = messageMapper.toEntity(event.getMessage());
        messagesDao.add(messageEntity);
        log.debug("event message is stored.");

        event.getMessage().setMessageId(messageEntity.getMessageId());
        EventHandler handler = handlers.get(event.getClass());

        if (handler != null) {
            try {
                log.debug("Handling event: {}", event);
                handler.handle(event);
            } catch (Exception e) {
                log.error("Error handling event: {}", event, e);
            }
        } else {
            throw new IllegalArgumentException("No handler registered for event type: " + event.getClass());
        }
    }
}
