package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.RoleEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface RolesMapper {

    @Select("SELECT * from roles WHERE role_id = #{roleId}")
    RoleEntity getById(Integer roleId);


    @Select("SELECT * FROM roles WHERE role_name = #{roleName}")
    RoleEntity getByName(String roleName);

    @Insert("INSERT INTO roles (role_type, role_name, role_description) VALUES (#{roleTypeValue, typeHandler=me.huangduo.hms.dao.handler.RoleTypeHandler}, #{roleName}, #{roleDescription})")
    int create(RoleEntity roleEntity);

    int update(RoleEntity roleEntity);

    @Delete("DELETE FROM roles WHERE role_id = #{roleId}")
    int delete(Integer roleId);
}
