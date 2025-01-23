package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class HomeAlreadyExistsException extends BusinessException {
    public HomeAlreadyExistsException() {
        super(HmsErrorCodeEnum.HOME_ERROR_201);
    }
}
