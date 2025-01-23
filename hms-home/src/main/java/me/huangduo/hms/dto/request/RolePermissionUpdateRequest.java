package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import me.huangduo.hms.enums.ErrorCode;

import java.util.List;

public record RolePermissionUpdateRequest(
        @NotEmpty(message = "permissionIds can not be empty.")
        List<@NotNull @Positive Integer> permissionIds
) implements HmsRequestBody {
        @Override
        public ErrorCode getErrorCode() {
                return ErrorCode.HOME_ERROR_2014;
        }
}
