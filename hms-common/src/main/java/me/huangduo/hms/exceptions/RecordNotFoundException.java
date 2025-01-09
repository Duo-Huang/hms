package me.huangduo.hms.exceptions;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public class RecordNotFoundException extends BusinessException {

    public RecordNotFoundException(HmsErrorCodeEnum hmsErrorCodeEnum) {
        super(hmsErrorCodeEnum);
    }
}