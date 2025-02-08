package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.ErrorCodeEnum;

public record UserProfileUpdateRequest(
        @NotEmpty(message = "nickname can not be empty.")
        @Size(max = 16, message = "nickname must be at most 16 characters long.")
        String nickname
) implements HmsRequestBody {
    @Override
    public ErrorCodeEnum getErrorCode() {
        return ErrorCodeEnum.USER_ERROR_106;
    }
}
