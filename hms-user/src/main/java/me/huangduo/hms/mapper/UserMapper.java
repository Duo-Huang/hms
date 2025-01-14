package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.UserEntity;
import me.huangduo.hms.dto.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserEntity toEntity(User user);

    <T> User toModel(T source);
}
