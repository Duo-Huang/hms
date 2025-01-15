package me.huangduo.hms.dao;

import me.huangduo.hms.dto.model.Home;
import me.huangduo.hms.dto.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommonDao {

    @Select("SELECT user_id, username, nickname, created_at, updated_at FROM users WHERE user_id = #{userId}")
    User getUserById(Integer userId);

    @Select("SELECT * FROM homes WHERE home_id = #{homeId}")
    Home getHomeById(Integer homeId);

    @Select("SELECT COUNT(*) FROM home_member_roles WHERE home_id = #{homeId} AND user_id = #{userId}")
    int isUserInHome(Integer homeId, Integer userId);
}
