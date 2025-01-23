package me.huangduo.hms.exceptions;

import lombok.Getter;
import me.huangduo.hms.enums.ErrorCode;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
