package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.model.Home;
import me.huangduo.hms.dto.request.HomeCreateOrUpdateRequest;
import me.huangduo.hms.dto.response.HomeInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HomeMapper extends BaseMapper<HomeEntity, Home> {

    Home toModel(HomeCreateOrUpdateRequest homeCreateOrUpdateRequest);

    HomeInfoResponse toResponse(Home home);
}
