package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.RoleEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolesDao {


    @Select("SELECT * FROM roles WHERE role_id = #{roleId}")
    RoleEntity getById(Integer roleId);

    @Select("SELECT * from roles WHERE role_id = #{roleId} AND home_id = #{homeId}")
    RoleEntity getItemByIdAndHomeId(Integer homeId, Integer roleId);

    @Select("SELECT * FROM roles WHERE home_id = #{homeId}")
    List<RoleEntity> getItemsByHomeId(Integer homeId);

    @Insert("INSERT INTO roles (role_type, role_name, role_description, home_id) VALUES (#{roleTypeValue}, #{roleName}, #{roleDescription}, #{homeId})")
    int add(RoleEntity roleEntity);

    int updateByIdAndHomeId(Integer homeId, Integer roleId);

    @Delete("DELETE FROM roles WHERE role_id = #{roleId} AND home_id=#{homeId}")
    int deleteByIdAndHomeId(Integer homeId, Integer roleId);
}
