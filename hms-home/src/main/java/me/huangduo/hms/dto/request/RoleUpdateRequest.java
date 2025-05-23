package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.Size;
import me.huangduo.hms.annotations.AtLeastOneNotNull;
import me.huangduo.hms.enums.ErrorCodeEnum;

@AtLeastOneNotNull(message = "roleName or roleDescription must be provided.")
public record RoleUpdateRequest(
        @Size(max = 12, message = "roleName must be at most 12 characters long.")
        String roleName,

        @Size(max = 100, message = "roleDescription must be at most 100 characters long.")
        String roleDescription
) implements HmsRequestBody {
    @Override
    public ErrorCodeEnum getErrorCode() {
        return ErrorCodeEnum.HOME_ERROR_212;
    }
}
