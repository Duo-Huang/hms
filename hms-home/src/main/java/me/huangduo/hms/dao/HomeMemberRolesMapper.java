package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.dto.model.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HomeMemberRolesMapper {

    @Insert("INSERT INTO home_member_roles (user_id, home_id) VALUES (#{userId}, #{homeId})")
    int addUserToTheHome(HomeMemberRoleEntity homeMemberRoleEntity);

    @Delete("DELETE FROM home_member_roles WHERE user_id = #{userId} AND home_id = #{homeId}")
    int removeMemberFromTheHome(HomeMemberRoleEntity homeMemberRoleEntity);

    @Update("UPDATE home_member_roles SET member_name = #{memberName} WHERE home_id = #{homeId} AND user_id = #{userId}")
    int updateMemberName(HomeMemberRoleEntity homeMemberRoleEntity);

    @Update("UPDATE home_member_roles SET role_id = #{roleId} WHERE user_id = #{userId} AND home_id = #{homeId}")
    int updateRoleForTheMember(HomeMemberRoleEntity homeMemberRoleEntity);

    @Select("SELECT role_id FROM home_member_roles WHERE user_id = #{userId} AND home_id = #{homeId}")
    Integer getRoleIdByHomeIdAndUserId(HomeMemberRoleEntity homeMemberRoleEntity);

    @Select("SELECT * FROM home_member_roles WHERE home_id = ${homeId}")
    List<HomeMemberRoleEntity> getItemsByHomeId(Integer homeId);

    @Select("SELECT home_id FROM home_member_roles WHERE user_id = ${userId}")
    List<Integer> getHomeIdsByUserId(Integer userId);

    @Select("SELECT hmr.*, r.role_name, r.role_description\n" +
            "FROM home_member_roles hmr\n" +
            "LEFT JOIN roles r ON hmr.role_id = r.role_id\n" +
            "WHERE hmr.home_id = 1;")
    List<Member> getMembersWithRolesByHomeId(Integer homeId); // 组合查询

}
