package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class HomeMemberAlreadyExistsException extends BusinessException {
    public HomeMemberAlreadyExistsException() {
        super(HmsErrorCodeEnum.HOME_ERROR_205);
    }
}
