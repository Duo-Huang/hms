package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.MessageEntity;
import me.huangduo.hms.enums.MessageTypeEnum;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessagesDao {

    @Insert("INSERT INTO messages (message_type, message_content, payload, expiration) VALUES (#{messageType}, #{messageContent}, #{payload}, #{expiration})")
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    void add(MessageEntity messageEntity);

    @Select("SELECT * FROM messages WHERE JSON_UNQUOTE(JSON_EXTRACT(payload, '$.homeId')) = #{homeId} AND expiration > NOW() ORDER BY created_at DESC")
    List<MessageEntity> getHistoryMessagesByHomeId(Integer homeId);

    @Select("SELECT * FROM messages WHERE JSON_UNQUOTE(JSON_EXTRACT(payload, '$.receiver.userId')) = #{userId} AND expiration > NOW() ORDER BY created_at DESC/* LIMIT 10*/")
    List<MessageEntity> getHistoryMessagesByReceiveUserId(Integer userId);

    @Select("SELECT * FROM messages WHERE message_type = #{messageTypeEnum} AND expiration > NOW() ORDER BY created_at DESC/* LIMIT 10*/")
    List<MessageEntity> getHistoryMessagesByType(MessageTypeEnum messageTypeEnum);

    @Select("SELECT * FROM messages WHERE (message_type = #{messageTypeEnum} OR JSON_UNQUOTE(JSON_EXTRACT(payload, '$.homeId')) = #{homeId}) AND expiration > NOW() ORDER BY created_at DESC/* LIMIT 10*/")
    List<MessageEntity> getHistoryMessagesByTypeOrHomeId(MessageTypeEnum messageTypeEnum, Integer homeId);
}
