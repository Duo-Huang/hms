package me.huangduo.hms.exceptions;

import lombok.Getter;
import me.huangduo.hms.enums.ErrorCodeEnum;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final ErrorCodeEnum errorCodeEnum;

    protected BusinessException(ErrorCodeEnum errorCodeEnum) {
        this.errorCodeEnum = errorCodeEnum;
    }
}
