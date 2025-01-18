package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record MemberRoleRequest(
        @NotNull(message = "roleId can not be null")
        @Positive(message = "roleId is a positive integer.")
        Integer roleId
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.HOME_ERROR_2012;
    }
}
