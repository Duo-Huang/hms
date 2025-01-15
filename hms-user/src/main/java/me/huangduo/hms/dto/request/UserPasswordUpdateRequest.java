package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record UserPasswordUpdateRequest(
        @NotNull(message = "oldPassword can not be null.")
        @Size(min = 6, max = 20, message = "The password length must be between 6 and 20 characters.")
        String oldPassword,

        @NotNull(message = "newPassword can not be null.")
        @Size(min = 6, max = 20, message = "The password length must be between 6 and 20 characters.")
        String newPassword
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.USER_ERROR_108;
    }
}
