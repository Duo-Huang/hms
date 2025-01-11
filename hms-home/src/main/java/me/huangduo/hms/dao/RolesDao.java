package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.RoleEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RolesDao {

    @Select("SELECT * from roles WHERE role_id = #{roleId}")
    RoleEntity getById(Integer roleId);

    @Select("SELECT * FROM roles WHERE role_name = ${roleName} AND role_type = 0")
    RoleEntity getSystemRoleByName(String roleName);

    @Insert("INSERT INTO roles (role_type, role_name, role_description) VALUES (#{roleTypeValue}, #{roleName}, #{roleDescription})")
    int create(RoleEntity roleEntity);

    int update(RoleEntity roleEntity);

    @Delete("DELETE FROM roles WHERE role_id = #{roleId}")
    int delete(Integer roleId);
}
