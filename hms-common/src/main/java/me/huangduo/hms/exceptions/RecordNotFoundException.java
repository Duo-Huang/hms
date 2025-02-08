package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCodeEnum;

public class RecordNotFoundException extends BusinessException {

    public RecordNotFoundException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum);
    }
}