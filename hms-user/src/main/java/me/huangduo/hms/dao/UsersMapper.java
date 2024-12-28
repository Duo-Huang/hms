package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.UserDao;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsersMapper {

    @Insert("INSERT INTO users (username, password, nickname) VALUES (#{username}, #{password}, #{nickname})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int create(UserDao user);

    @Select("SELECT * FROM users where username = #{username} and password = #{password}")
    UserDao findUserByUsernameAndPassword(String username, String password);

}
