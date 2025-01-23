package me.huangduo.hms.exceptions;

import lombok.Getter;
import me.huangduo.hms.enums.HmsErrorCodeEnum;

@Getter
public abstract class BusinessException extends RuntimeException {
    private final HmsErrorCodeEnum hmsErrorCodeEnum;

    public BusinessException(HmsErrorCodeEnum hmsErrorCodeEnum) {
        this.hmsErrorCodeEnum = hmsErrorCodeEnum;
    }
}
