package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.MessageEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessagesDao {

    @Insert("INSERT INTO messages (message_type, message_content, payload, expiration) VALUES (#{messageType}, #{messageContent}, #{payload}, #{expiration})")
    void add(MessageEntity messageEntity);
}
