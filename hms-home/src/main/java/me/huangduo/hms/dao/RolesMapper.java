package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.RoleEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RolesMapper {

    @Select("SELECT * from roles WHERE role_id = #{roleId}")
    RoleEntity getById(Integer roleId);


    @Insert("INSERT INTO roles (role_type, role_name, role_description) VALUES (#{roleTypeValue, typeHandler=me.huangduo.hms.dao.typeHandler.RoleTypeHandler}, #{roleName}, #{roleDescription})")
    int create(RoleEntity roleEntity);

    int update(RoleEntity roleEntity);

    @Delete("DELETE FROM roles WHERE role_id = #{roleId}")
    int delete(Integer roleId);
}
