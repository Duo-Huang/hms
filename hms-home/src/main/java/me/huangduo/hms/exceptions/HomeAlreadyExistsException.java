package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class HomeAlreadyExistsException extends BusinessException {
    public HomeAlreadyExistsException() {
        super(ErrorCodeEnum.HOME_ERROR_201);
    }
}
