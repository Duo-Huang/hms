package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class HomeMemberAlreadyExistsException extends BusinessException {
    public HomeMemberAlreadyExistsException() {
        super(ErrorCode.HOME_ERROR_205);
    }
}
