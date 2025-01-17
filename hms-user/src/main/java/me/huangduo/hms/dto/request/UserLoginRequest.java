package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record UserLoginRequest(
        @NotEmpty(message = "username can not be empty.")
        String username,

        @NotEmpty(message = "password can not be empty.")
        String password
) implements HmsRequest {

    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.USER_ERROR_106;
    }
}
