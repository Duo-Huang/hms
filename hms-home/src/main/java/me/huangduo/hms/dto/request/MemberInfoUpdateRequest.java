package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.HmsRequest;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record MemberInfoUpdateRequest(
        @NotNull(message = "home member name can not be null")
        @Size(max = 16, message = "home name must be at most 16 characters long")
        String memberName
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.HOME_ERROR_207;
    }
}
