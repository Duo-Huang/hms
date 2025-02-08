package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class DuplicatedPasswordException extends BusinessException {
    public DuplicatedPasswordException() {
        super(ErrorCodeEnum.USER_ERROR_109);
    }
}
