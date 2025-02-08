package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class HomeMemberAlreadyExistsException extends BusinessException {
    public HomeMemberAlreadyExistsException() {
        super(ErrorCodeEnum.HOME_ERROR_205);
    }
}
