package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class InvitationCodeExpiredException extends BusinessException {
    public InvitationCodeExpiredException() {
        super(HmsErrorCodeEnum.HOME_ERROR_2017);
    }
}
