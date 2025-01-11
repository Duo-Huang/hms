package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.UserEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsersDao {

    @Insert("INSERT INTO users (username, password, nickname) VALUES (#{username}, #{password}, #{nickname})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int create(UserEntity user);

    @Select("SELECT * FROM users where username = #{username} and password = #{password}")
    UserEntity findUserByUsernameAndPassword(String username, String password);

    @Select("SELECT user_id, username, nickname, created_at, updated_at FROM users WHERE user_id = #{userId}")
    UserEntity getUserInfoById(Integer userId);

    void update(UserEntity userEntity);
}
