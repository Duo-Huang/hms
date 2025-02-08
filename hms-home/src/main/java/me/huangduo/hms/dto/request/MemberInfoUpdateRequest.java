package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.ErrorCodeEnum;

public record MemberInfoUpdateRequest(
        @NotEmpty(message = "memberName can not be empty.")
        @Size(max = 16, message = "memberName must be at most 16 characters long.")
        String memberName
) implements HmsRequestBody {
    @Override
    public ErrorCodeEnum getErrorCode() {
        return ErrorCodeEnum.HOME_ERROR_207;
    }
}
