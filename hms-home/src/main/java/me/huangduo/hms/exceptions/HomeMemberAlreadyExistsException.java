package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class HomeMemberAlreadyExistsException extends BusinessException {
    public HomeMemberAlreadyExistsException(HmsErrorCodeEnum hmsErrorCodeEnum) {
        super(hmsErrorCodeEnum);
    }
}
