package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.UserEntity;
import me.huangduo.hms.dto.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserEntity toEntity(User user);

    User toModel(UserEntity userEntity);
}
