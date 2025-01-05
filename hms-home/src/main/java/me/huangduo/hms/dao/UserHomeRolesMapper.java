package me.huangduo.hms.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserHomeRolesMapper {

    @Insert("INSERT INTO user_home_roles (user_id, home_id) VALUES (#{userId}, #{homeId})")
    int addUserToTheHome(Integer userId, Integer homeId);

    @Delete("DELETE FROM user_home_roles WHERE user_id = #{userId} AND home_id = #{homeId}")
    int removeUserFromTheHome(Integer userId, Integer homeId);

    @Update("UPDATE user_home_roles SET role_id = #{roleId} WHERE user_id = #{userId} AND home_id = #{homeId}")
    int updateRoleForTheHomeUser(Integer userId, Integer homeId, Integer roleId);

    @Update("UPDATE user_home_roles SET role_id = null WHERE user_id = #{userId} AND home_id = #{homeId}")
    int removeRoleForTheHomeUser(Integer userId, Integer homeId);

    @Select("SELECT role_id FROM user_home_roles WHERE user_id = #{userId} AND home_id = #{homeId}")
    Integer getRoleIdByHomeIdAndUserId(Integer userId, Integer homeId);

    @Select("SELECT user_id FROM user_home_roles WHERE home_id = ${homeId}")
    List<Integer> getUserIdsByHomeId(Integer homeId);

    @Select("SELECT home_id FROM user_home_roles WHERE user_id = ${userId}")
    List<Integer> getHomeIdsByUserId(Integer userId);
}
