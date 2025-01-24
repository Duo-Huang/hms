package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.events.HmsEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface MessagesDao {

    @Insert("INSERT INTO messages (message_type, message_content, payload, expiration, home_id) VALUES (#{messageType}, #{messageContent}, #{payload}, #{expiration}, #{homeId})")
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    void add(MessageEntity<? extends HmsEvent.MessagePayload> messageEntity);
}
