package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class IllegalAssignRoleException extends BusinessException {
    public IllegalAssignRoleException() {
        super(ErrorCodeEnum.HOME_ERROR_2020);
    }
}
