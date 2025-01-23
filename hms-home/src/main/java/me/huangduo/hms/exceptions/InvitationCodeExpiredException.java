package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class InvitationCodeExpiredException extends BusinessException {
    public InvitationCodeExpiredException() {
        super(ErrorCode.HOME_ERROR_2017);
    }
}
