package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record RoleCreateRequest(
        @NotNull
        @Positive(message = "system role id is a positive integer.")
        Integer systemRoleId,

        @NotNull
        @Size(max = 12, message = "role name must be at most 12 characters long")
        String roleName,

        @Size(max = 100, message = "role name must be at most 100 characters long")
        String roleDescription
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.HOME_ERROR_2015;
    }
}
