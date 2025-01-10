package me.huangduo.hms.dao;

import me.huangduo.hms.dao.entity.PermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PermissionsMapper {

    @Select("SELECT * FROM permissions WHERE permission_id = #{permissionId}")
    PermissionEntity getById(Integer permissionId);
}
