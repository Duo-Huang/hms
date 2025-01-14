package me.huangduo.hms.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import me.huangduo.hms.dto.request.HmsRequest;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record MemberRoleRequest(
        @NotNull
        @Positive(message = "role id is a positive integer.")
        Integer roleId
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.HOME_ERROR_2012;
    }
}
