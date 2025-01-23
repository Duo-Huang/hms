package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class RoleAlreadyExistedException extends BusinessException {
    public RoleAlreadyExistedException() {
        super(HmsErrorCodeEnum.HOME_ERROR_2010);
    }
}
