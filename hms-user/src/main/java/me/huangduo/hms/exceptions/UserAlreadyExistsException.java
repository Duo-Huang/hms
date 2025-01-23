package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException() {
        super(ErrorCode.USER_ERROR_103);
    }
}
