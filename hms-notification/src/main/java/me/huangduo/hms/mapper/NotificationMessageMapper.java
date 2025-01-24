package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.events.NotificationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMessageMapper extends MessageMapper<MessageEntity<NotificationEvent.NotificationMessagePayload>, Message<NotificationEvent.NotificationMessagePayload>> {

}
