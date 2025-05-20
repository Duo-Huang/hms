package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.ErrorCodeEnum;

public record HmsResponseBody<T>(int code, String message, T data) {

    public static <T> HmsResponseBody<T> success(T data) {
        return new HmsResponseBody<>(0, null, data);
    }

    public static <T> HmsResponseBody<T> success() {
        return success(null);
    }

    public static <T> HmsResponseBody<T> error(ErrorCodeEnum errorCodeEnum, T data) {
        return new HmsResponseBody<>(errorCodeEnum.getCode(), errorCodeEnum.getMessage(), data);
    }

    public static <T> HmsResponseBody<T> error(ErrorCodeEnum errorCodeEnum) {
        return error(errorCodeEnum, null);
    }

    public static <T> HmsResponseBody<T> error(int code, String message, T data) {
        return new HmsResponseBody<>(code, message, data);
    }

    public static <T> HmsResponseBody<T> error(int code, String message) {
        return new HmsResponseBody<>(code, message, null);
    }
}
