package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.PermissionEntity;
import me.huangduo.hms.dao.entity.RolePermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolePermissionsDao {

    @Select("SELECT * FROM role_permissions WHERE role_id = #{roleId}")
    List<RolePermissionEntity> getItemByRoleId(Integer roleId);


    @Select("SELECT * FROM role_permissions rp, permissions p WHERE rp.permission_id = p.permission_id AND rp.role_id = #{roleId}")
    List<PermissionEntity> getPermissionsByRoleId(Integer roleId);

    int batchCreate(List<RolePermissionEntity> permissionEntities);

    int batchDelete(List<RolePermissionEntity> permissionEntities);
}
