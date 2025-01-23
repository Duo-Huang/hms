package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class AuthenticationException extends BusinessException {
    public AuthenticationException() {
        super(ErrorCode.USER_ERROR_101);
    }
}
