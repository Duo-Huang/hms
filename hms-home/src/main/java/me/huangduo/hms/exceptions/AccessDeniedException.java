package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException() {
        super(ErrorCode.HOME_ERROR_2013);
    }

    public AccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
