package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record UserLoginRequest(
        @NotNull(message = "username can not be null.")
        String username,

        @NotNull(message = "password can not be null.")
        String password
) implements HmsRequest {

    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.USER_ERROR_106;
    }
}
