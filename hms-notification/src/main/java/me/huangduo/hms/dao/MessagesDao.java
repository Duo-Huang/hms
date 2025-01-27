package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.MessageEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessagesDao {

    @Insert("INSERT INTO messages (message_type, message_content, payload, expiration, home_id) VALUES (#{messageType}, #{messageContent}, #{payload}, #{expiration}, #{homeId})")
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    void add(MessageEntity messageEntity);

    @Select("SELECT * FROM messages WHERE home_id = #{homeId} AND expiration > NOW() ORDER BY created_at DESC LIMIT 10")
    List<MessageEntity> getHistoryMessages(Integer homeId);
}
