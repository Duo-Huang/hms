package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.events.HmsEvent;

public interface MessageMapper<E extends MessageEntity<? extends HmsEvent.MessagePayload>, M extends Message<? extends HmsEvent.MessagePayload>> {
    E toEntity(M m);

    M toModel(E e);
}
