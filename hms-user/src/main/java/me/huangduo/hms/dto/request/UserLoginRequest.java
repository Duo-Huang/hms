package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import me.huangduo.hms.enums.ErrorCodeEnum;

public record UserLoginRequest(
        @NotEmpty(message = "username can not be empty.")
        String username,

        @NotEmpty(message = "password can not be empty.")
        String password
) implements HmsRequestBody {

    @Override
    public ErrorCodeEnum getErrorCode() {
        return ErrorCodeEnum.USER_ERROR_105;
    }
}
