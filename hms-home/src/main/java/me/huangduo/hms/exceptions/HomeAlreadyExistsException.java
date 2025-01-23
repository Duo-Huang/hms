package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class HomeAlreadyExistsException extends BusinessException {
    public HomeAlreadyExistsException() {
        super(ErrorCode.HOME_ERROR_201);
    }
}
