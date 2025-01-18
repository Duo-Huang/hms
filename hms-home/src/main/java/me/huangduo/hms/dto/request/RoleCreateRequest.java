package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record RoleCreateRequest(
        @NotNull(message = "baseRoleId can not be null")
        @Positive(message = "baseRoleId is a positive integer.")
        Integer baseRoleId,

        @NotEmpty(message = "roleName can not be empty.")
        @Size(max = 12, message = "roleName must be at most 12 characters long")
        String roleName,

        @Size(max = 100, message = "roleDescription must be at most 100 characters long")
        String roleDescription
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.HOME_ERROR_2012;
    }
}
