package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class RoleAlreadyExistedException extends BusinessException {
    public RoleAlreadyExistedException() {
        super(ErrorCodeEnum.HOME_ERROR_2010);
    }
}
