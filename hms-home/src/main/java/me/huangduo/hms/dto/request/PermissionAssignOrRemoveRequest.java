package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

import java.util.List;

public record PermissionAssignOrRemoveRequest(
        @NotEmpty(message = "permissionIds can not be empty.")
        List<@NotNull @Positive Integer> permissionIds
) implements HmsRequest {
        @Override
        public HmsErrorCodeEnum getHmsErrorCodeEnum() {
                return HmsErrorCodeEnum.HOME_ERROR_2017;
        }
}
