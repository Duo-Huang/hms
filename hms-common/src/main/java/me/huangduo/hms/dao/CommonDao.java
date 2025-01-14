package me.huangduo.hms.dao;

import me.huangduo.hms.dto.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommonDao {

    @Select("SELECT user_id, username, nickname, created_at, updated_at FROM users WHERE user_id = #{userId}")
    User getUserInfoById(Integer userId);
}
