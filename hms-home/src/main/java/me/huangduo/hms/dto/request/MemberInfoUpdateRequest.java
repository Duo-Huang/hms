package me.huangduo.hms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import me.huangduo.hms.enums.ErrorCode;

public record MemberInfoUpdateRequest(
        @NotEmpty(message = "memberName can not be empty.")
        @Size(max = 16, message = "memberName must be at most 16 characters long.")
        String memberName
) implements HmsRequestBody {
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.HOME_ERROR_207;
    }
}
