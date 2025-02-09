package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.PermissionEntity;
import me.huangduo.hms.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {

    Permission toModel(PermissionEntity permissionEntity);
}
