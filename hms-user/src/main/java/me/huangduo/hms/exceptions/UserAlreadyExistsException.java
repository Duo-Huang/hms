package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException() {
        super(HmsErrorCodeEnum.USER_ERROR_103);
    }
}
