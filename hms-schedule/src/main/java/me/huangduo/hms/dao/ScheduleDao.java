package me.huangduo.hms.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduleDao {

    @Delete("DELETE FROM messages WHERE expiration < now()")
    int deleteExpiredMessages();

    @Delete("DELETE FROM revoked_tokens WHERE expiration < now()")
    int deleteExpiredRevokedTokens();
}
