package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class AuthenticationException extends BusinessException {
    public AuthenticationException() {
        super(HmsErrorCodeEnum.USER_ERROR_101);
    }
}
