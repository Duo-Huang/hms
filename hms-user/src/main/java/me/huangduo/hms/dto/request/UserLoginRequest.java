package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import me.huangduo.hms.enums.ErrorCode;

public record UserLoginRequest(
        @NotEmpty(message = "username can not be empty.")
        String username,

        @NotEmpty(message = "password can not be empty.")
        String password
) implements HmsRequestBody {

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.USER_ERROR_105;
    }
}
