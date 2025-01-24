package me.huangduo.hms.service;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dao.MessagesDao;
import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.dto.model.Message;
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
    private final Map<Class<? extends HmsEvent<? extends HmsEvent.MessagePayload>>, EventHandler> handlers = new HashMap<>();
    private final Map<Class<? extends HmsEvent<? extends HmsEvent.MessagePayload>>, MessageMapper<? extends MessageEntity<? extends HmsEvent.MessagePayload>, ? extends Message<? extends HmsEvent.MessagePayload>>> mappers = new HashMap<>();

    private final MessagesDao messagesDao;

    public EventHandlerServiceImpl(MessagesDao messagesDao) {
        this.messagesDao = messagesDao;
    }

    @Override
    public void registerHandler(Class<? extends HmsEvent<? extends HmsEvent.MessagePayload>> eventType, EventHandler handler) {
        log.debug("Registering handler for event type: {}", eventType);
        handlers.put(eventType, handler);
    }

    @Override
    public void registerMessageMapper(Class<? extends HmsEvent<? extends HmsEvent.MessagePayload>> eventType, MessageMapper<? extends MessageEntity<? extends HmsEvent.MessagePayload>, ? extends Message<? extends HmsEvent.MessagePayload>> mapper) {
        log.debug("Registering message mapper for event type: {}", eventType);
        mappers.put(eventType, mapper);
    }


    @Override
    @EventListener
    @SuppressWarnings("unchecked")
    public void handleEvent(HmsEvent<? extends HmsEvent.MessagePayload> event) {
        log.info("Received event: {}", event);

        MessageMapper<MessageEntity<? extends HmsEvent.MessagePayload>, Message<? extends HmsEvent.MessagePayload>> messageMapper =
                (MessageMapper<MessageEntity<? extends HmsEvent.MessagePayload>, Message<? extends HmsEvent.MessagePayload>>) mappers.get(event.getClass());

        if (messageMapper == null) {
            throw new IllegalArgumentException("No mapper registered for event type: " + event.getClass());
        }

        MessageEntity<? extends HmsEvent.MessagePayload> messageEntity = messageMapper.toEntity(event.getMessage());

        messagesDao.add(messageEntity);
        log.debug("event message is stored.");
        
        event.getMessage().setMessageId(messageEntity.getMessageId());
        EventHandler handler = handlers.get(event.getClass());

        if (handler != null) {
            log.debug("Handling event: {}", event);
            handler.handle(event);
        } else {
            throw new IllegalArgumentException("No handler registered for event type: " + event.getClass());
        }
    }
}
