package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class RoleAlreadyExistedException extends BusinessException {
    public RoleAlreadyExistedException() {
        super(ErrorCode.HOME_ERROR_2010);
    }
}
