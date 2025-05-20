package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import me.huangduo.hms.enums.ErrorCodeEnum;

public record MemberRoleRequest(
        @NotNull(message = "roleId can not be null")
        @Positive(message = "roleId is a positive integer.")
        Integer roleId
) implements HmsRequestBody {
    @Override
    public ErrorCodeEnum getErrorCode() {
        return ErrorCodeEnum.HOME_ERROR_212;
    }
}
