package me.huangduo.hms.mapper;

import me.huangduo.hms.dto.model.Member;
import me.huangduo.hms.dto.response.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {

    <T> Member toModel(T source);

    MemberResponse toResponse(Member member);
}
