package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class DuplicatedPasswordException extends BusinessException {
    public DuplicatedPasswordException() {
        super(HmsErrorCodeEnum.USER_ERROR_109);
    }
}
