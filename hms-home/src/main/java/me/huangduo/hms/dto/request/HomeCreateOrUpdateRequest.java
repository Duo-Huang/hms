package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.Size;
import me.huangduo.hms.annotations.AtLeastOneNotNull;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

@AtLeastOneNotNull(message = "homeName or homeDescription must be provided.")
public record HomeCreateOrUpdateRequest(
        @Size(min = 1, max = 16, message = "The length of the home name must be between 1 and 16.")
        String homeName,

        @Size(max = 50, message = "home description must be at most 50 characters long.")
        String homeDescription
) implements HmsRequest {
    @Override
    public HmsErrorCodeEnum getHmsErrorCodeEnum() {
        return HmsErrorCodeEnum.HOME_ERROR_202;
    }
}
