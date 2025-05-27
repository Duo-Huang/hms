package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.ErrorCodeEnum;

public record UserRegistrationRequest(
        @NotEmpty(message = "username can not be empty.")
        @Size(min = 6, max = 16, message = "username must be between 6 and 16 characters.")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "username can only contain letters, numbers, underscores, and hyphens.")
        String username,

        @NotBlank(message = "password can not be empty.")
        @Size(min = 6, max = 20, message = "The password length must be between 6 and 20 characters.")
        String password
) implements HmsRequestBody {
    @Override
    public ErrorCodeEnum getErrorCode() {
        return ErrorCodeEnum.USER_ERROR_104;
    }
}
