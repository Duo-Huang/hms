package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.HomeEntity;
import me.huangduo.hms.dto.model.Home;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HomeMapper {

    HomeEntity toEntity(Home home);

    Home toModel(HomeEntity homeEntity);
}
