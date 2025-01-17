package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record UserAcceptHomeInvitationRequest(
        @NotEmpty(message = "invitationCode can not be empty.")
        @Pattern(regexp = "^[AHVE8S2DZX9C7P5IK3MJUFR4WYLTN6BGQ]{6}$", message = "invalid invitationCode")
        String invitationCode
) implements HmsRequest {
        @Override
        public HmsErrorCodeEnum getHmsErrorCodeEnum() {
                return HmsErrorCodeEnum.HOME_ERROR_2018;
        }
}
