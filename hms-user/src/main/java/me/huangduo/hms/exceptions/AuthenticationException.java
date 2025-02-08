package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class AuthenticationException extends BusinessException {
    public AuthenticationException() {
        super(ErrorCodeEnum.USER_ERROR_101);
    }
}
