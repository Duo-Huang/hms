package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class DuplicatedPasswordException extends BusinessException {
    public DuplicatedPasswordException() {
        super(ErrorCode.USER_ERROR_109);
    }
}
