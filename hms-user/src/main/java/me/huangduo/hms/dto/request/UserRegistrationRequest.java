package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record UserRegistrationRequest(
        @NotNull(message = "username can not be null.")
        @Size(max = 16, message = "username must be at most 16 characters long")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "username can only contain letters, numbers, underscores, and hyphens")
        String username,

        @NotNull(message = "password can not be null.")
        @Size(min = 6, max = 20, message = "The password length must be between 6 and 20 characters.")
        String password
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.USER_ERROR_105;
    }
}
