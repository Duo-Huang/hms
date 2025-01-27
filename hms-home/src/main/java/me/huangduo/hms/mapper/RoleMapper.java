package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.RoleEntity;
import me.huangduo.hms.dto.model.HomeRole;
import me.huangduo.hms.dto.request.RoleCreateRequest;
import me.huangduo.hms.dto.request.RoleUpdateRequest;
import me.huangduo.hms.dto.response.RoleResponse;
import me.huangduo.hms.dto.response.RoleWithPermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper extends BaseMapper<RoleEntity, HomeRole> {

    HomeRole toModel(RoleCreateRequest roleCreateRequest);

    HomeRole toModel(RoleUpdateRequest roleUpdateRequest);

    //    @Mapping(source = "roleType", target = "roleType", qualifiedByName = "RoleTypeToInt")
    RoleResponse toResponse(HomeRole role);

//    @Named("RoleTypeToInt")
//    default int RoleTypeToInt(RoleType roleType) {
//        return roleType != null ? roleType.getValue() : -1;
//    }

    RoleWithPermissionResponse toResponseWithPermission(HomeRole role);
}
