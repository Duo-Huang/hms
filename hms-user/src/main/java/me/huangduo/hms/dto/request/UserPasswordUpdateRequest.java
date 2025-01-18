package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record UserPasswordUpdateRequest(
        @NotEmpty(message = "oldPassword can not be empty.")
        @Size(min = 6, max = 20, message = "The password length must be between 6 and 20 characters.")
        String oldPassword,

        @NotEmpty(message = "newPassword can not be empty.")
        @Size(min = 6, max = 20, message = "The password length must be between 6 and 20 characters.")
        String newPassword
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.USER_ERROR_107;
    }
}
