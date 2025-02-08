package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.dto.model.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HomeMemberRolesDao {

    @Insert("INSERT INTO home_member_roles (user_id, home_id, role_id, member_name) VALUES (#{userId}, #{homeId}, #{roleId}, #{memberName})")
    int add(HomeMemberRoleEntity homeMemberRoleEntity);

    @Delete("DELETE FROM home_member_roles WHERE user_id = #{userId} AND home_id = #{homeId}")
    int removeItemByUserIdAndHomeId(HomeMemberRoleEntity homeMemberRoleEntity);

    @Update("UPDATE home_member_roles SET member_name = #{memberName} WHERE home_id = #{homeId} AND user_id = #{userId}")
    int updateMemberNameByUserIdAndHomeId(HomeMemberRoleEntity homeMemberRoleEntity);

    @Update("UPDATE home_member_roles SET role_id = #{roleId} WHERE user_id = #{userId} AND home_id = #{homeId}")
    int updateRoleByUserIdAndHomeId(HomeMemberRoleEntity homeMemberRoleEntity);

    @Select("SELECT * FROM home_member_roles WHERE home_id = ${homeId}")
    List<HomeMemberRoleEntity> getItemsByHomeId(Integer homeId);

    @Select("SELECT * FROM home_member_roles WHERE home_id = #{homeId} AND user_id = ${userId}")
    HomeMemberRoleEntity getItemByHomeIdAndUserId(Integer homeId, Integer userId);

    @Select("SELECT home_id FROM home_member_roles WHERE user_id = ${userId}")
    List<Integer> getHomeIdsByUserId(Integer userId);

    @Select("SELECT hmr.member_name, hmr.created_at AS hmr_created_at, hmr.updated_at AS hmr_updated_at, r.*, u.user_id, u.username\n" +
            "FROM home_member_roles hmr\n" +
            "LEFT JOIN roles r ON hmr.role_id = r.role_id\n" +
            "LEFT JOIN users u ON hmr.user_id = u.user_id\n" +
            "WHERE hmr.home_id = #{homeId}")
    @Results({
            @Result(property = "createdAt", column = "hmr_created_at"),
            @Result(property = "updatedAt", column = "hmr_updated_at"),
            @Result(property = "role.roleId", column = "role_id"),
            @Result(property = "role.roleType", column = "role_type"),
            @Result(property = "role.roleName", column = "role_name"),
            @Result(property = "role.roleDescription", column = "role_description"),
            @Result(property = "role.createdAt", column = "created_at"),
            @Result(property = "role.updatedAt", column = "updated_at"),
    })
    List<Member> getMembersWithRolesByHomeId(Integer homeId); // 组合查询

    @Select("SELECT hmr.member_name, hmr.created_at AS hmr_created_at, hmr.updated_at AS hmr_updated_at, r.*, u.user_id, u.username\n" +
            "FROM home_member_roles hmr\n" +
            "LEFT JOIN roles r ON hmr.role_id = r.role_id\n" +
            "LEFT JOIN users u ON hmr.user_id = u.user_id\n" +
            "WHERE hmr.home_id = #{homeId} AND hmr.user_id = #{userId};")
    @Results({
            @Result(property = "createdAt", column = "hmr_created_at"),
            @Result(property = "updatedAt", column = "hmr_updated_at"),
            @Result(property = "role.roleId", column = "role_id"),
            @Result(property = "role.roleType", column = "role_type"),
            @Result(property = "role.roleName", column = "role_name"),
            @Result(property = "role.roleDescription", column = "role_description"),
            @Result(property = "role.createdAt", column = "created_at"),
            @Result(property = "role.updatedAt", column = "updated_at"),
    })
    Member getMemberWithRoleByHomeIdAndUserId(Integer homeId, Integer userId); // 组合查询

}
