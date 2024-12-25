package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.UserDao;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsersMapper {

    @Insert("INSERT INTO users (username, password, nickname, created_at, updated_at) VALUES (#{username}, #{password}, #{nickname}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int create(UserDao user);

    @Select("SELECT * FROM users where username = #{username}")
    UserDao findUserByUsername(String username);

}
