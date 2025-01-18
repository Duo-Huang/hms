package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.*;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record MemberInvitationRequest(
        @NotEmpty(message = "username can not be empty.")
        @Size(max = 16, message = "username must be at most 16 characters long.")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "username can only contain letters, numbers, underscores, and hyphens.")
        String username
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.HOME_ERROR_209;
    }
}
