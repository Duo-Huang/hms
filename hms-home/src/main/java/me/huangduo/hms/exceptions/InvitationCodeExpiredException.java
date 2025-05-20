package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class InvitationCodeExpiredException extends BusinessException {
    public InvitationCodeExpiredException() {
        super(ErrorCodeEnum.HOME_ERROR_217);
    }
}
