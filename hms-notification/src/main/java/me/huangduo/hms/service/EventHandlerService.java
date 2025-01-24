package me.huangduo.hms.service;

import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.events.EventHandler;
import me.huangduo.hms.events.HmsEvent;
import me.huangduo.hms.mapper.MessageMapper;

public interface EventHandlerService {
    void registerHandler(Class<? extends HmsEvent<? extends HmsEvent.MessagePayload>> eventType, EventHandler handler);

    void registerMessageMapper(Class<? extends HmsEvent<? extends HmsEvent.MessagePayload>> eventType, MessageMapper<? extends MessageEntity<? extends HmsEvent.MessagePayload>, ? extends Message<? extends HmsEvent.MessagePayload>> mapper);

    void handleEvent(HmsEvent<? extends HmsEvent.MessagePayload> event);
}
