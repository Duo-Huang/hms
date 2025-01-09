package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class HomeAlreadyExistsException extends BusinessException {
    public HomeAlreadyExistsException(HmsErrorCodeEnum hmsErrorCodeEnum) {
        super(hmsErrorCodeEnum);
    }
}
