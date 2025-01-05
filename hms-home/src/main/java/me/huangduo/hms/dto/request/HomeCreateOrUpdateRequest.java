package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.HmsRequest;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record HomeCreateOrUpdateRequest(
        @NotNull(message = "home name can not be null")
        @Size(max = 16, message = "home name must be at most 16 characters long")
        String homeName,

        @Size(max = 50, message = "home description must be at most 50 characters long")
        String homeDescription
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.HOME_ERROR_202;
    }
}
