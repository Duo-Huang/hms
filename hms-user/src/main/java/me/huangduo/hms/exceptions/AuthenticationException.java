package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class AuthenticationException extends BusinessException {
    public AuthenticationException(HmsErrorCodeEnum hmsErrorCodeEnum) {
        super(hmsErrorCodeEnum);
    }
}
