package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.UserEntity;
import me.huangduo.hms.dto.model.User;
import me.huangduo.hms.dto.request.UserLoginRequest;
import me.huangduo.hms.dto.request.UserRegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserEntity toEntity(User user);

    User toModel(UserEntity userEntity);

    User toModel(UserRegistrationRequest userRegistrationRequest);

    User toModel(UserLoginRequest userLoginRequest);
}
