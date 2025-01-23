package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import me.huangduo.hms.enums.ErrorCode;

public record UserAcceptHomeInvitationRequest(
        @NotEmpty(message = "invitationCode can not be empty.")
        @Pattern(regexp = "^[AHVE8S2DZX9C7P5IK3MJUFR4WYLTN6BGQ]{6}$", message = "invalid invitationCode")
        String invitationCode
) implements HmsRequestBody {
        @Override
        public ErrorCode getErrorCode() {
                return ErrorCode.HOME_ERROR_2015;
        }
}
