package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.ErrorCode;

public class RecordNotFoundException extends BusinessException {

    public RecordNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}