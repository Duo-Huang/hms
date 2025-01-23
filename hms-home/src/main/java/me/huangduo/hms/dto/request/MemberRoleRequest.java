package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import me.huangduo.hms.enums.ErrorCode;

public record MemberRoleRequest(
        @NotNull(message = "roleId can not be null")
        @Positive(message = "roleId is a positive integer.")
        Integer roleId
) implements HmsRequestBody {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.HOME_ERROR_2012;
    }
}
