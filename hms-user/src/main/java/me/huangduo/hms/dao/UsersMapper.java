package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.UserEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsersMapper {

    @Insert("INSERT INTO users (username, password, nickname) VALUES (#{username}, #{password}, #{nickname})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int create(UserEntity user);

    @Select("SELECT * FROM users where username = #{username} and password = #{password}")
    UserEntity findUserByUsernameAndPassword(String username, String password);

}
