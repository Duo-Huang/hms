package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException() {
        super(HmsErrorCodeEnum.HOME_ERROR_2013);
    }

    public AccessDeniedException(HmsErrorCodeEnum hmsErrorCodeEnum) {
        super(hmsErrorCodeEnum);
    }
}
