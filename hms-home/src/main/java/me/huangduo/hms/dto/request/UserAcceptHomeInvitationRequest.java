package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import org.hibernate.validator.constraints.UUID;

public record UserAcceptHomeInvitationRequest(
        @NotNull(message = "invitationCode can not be null.")
        @NotEmpty(message = "invitationCode can not be empty.")
        @UUID   // 更多校验, 比如uuid
        String invitationCode
) implements HmsRequest {
        @Override
        public HmsErrorCodeEnum getHmsErrorCodeEnum() {
                return HmsErrorCodeEnum.HOME_ERROR_206;
        }
}
