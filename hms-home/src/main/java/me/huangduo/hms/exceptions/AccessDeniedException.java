package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException() {
        super(ErrorCodeEnum.HOME_ERROR_213);
    }

    public AccessDeniedException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum);
    }
}
