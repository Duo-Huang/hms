package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record UserProfileUpdateRequest(
        @NotNull(message = "nickname can not be null.")
        @Size(max = 16, message = "nickname must be at most 16 characters long")
        String nickname
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.USER_ERROR_107;
    }
}
