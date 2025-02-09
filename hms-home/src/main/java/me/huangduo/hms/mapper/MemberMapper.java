package me.huangduo.hms.mapper;

import me.huangduo.hms.dao.entity.HomeMemberRoleEntity;
import me.huangduo.hms.model.Member;
import me.huangduo.hms.dto.request.MemberInfoUpdateRequest;
import me.huangduo.hms.dto.response.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {
    HomeMemberRoleEntity toEntity(Member member);

    Member toModel(MemberInfoUpdateRequest memberInfoUpdateRequest);

    MemberResponse toResponse(Member member);
}
