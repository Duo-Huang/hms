package me.huangduo.hms.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommonMapper {

    @Select("SELECT user_id FROM users WHERE user_id = #{userId}")
    Integer getUserIdById(Integer userId);
}
