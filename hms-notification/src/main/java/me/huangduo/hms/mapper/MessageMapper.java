package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.dto.model.Message;
import me.huangduo.hms.dto.response.MessageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MessageMapper extends BaseMapper<MessageEntity, Message> {

    @Mapping(target = "payload", expression = "java(message.getDeserializedPayload())")
    MessageResponse toResponse(Message message);
}
