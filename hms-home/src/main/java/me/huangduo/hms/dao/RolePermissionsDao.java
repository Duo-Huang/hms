package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.PermissionEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RolePermissionsDao {

    @Select("SELECT * FROM role_permissions WHERE role_id = #{roleId}")
    List<PermissionEntity> getPermissionsByRoleId(Integer roleId);
}
