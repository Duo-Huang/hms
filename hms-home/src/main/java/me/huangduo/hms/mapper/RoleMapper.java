package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.RoleEntity;
import me.huangduo.hms.dto.model.Role;
import me.huangduo.hms.dto.request.RoleCreateRequest;
import me.huangduo.hms.dto.request.RoleUpdateRequest;
import me.huangduo.hms.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    RoleEntity toEntity(Role home);

    Role toModel(RoleEntity roleEntity);

    Role toModel(RoleCreateRequest roleCreateRequest);

    Role toModel(RoleUpdateRequest roleUpdateRequest);

    RoleResponse toResponse(Role role);
}
