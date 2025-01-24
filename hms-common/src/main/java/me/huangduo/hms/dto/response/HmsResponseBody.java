package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.ErrorCode;

public record HmsResponseBody<T>(int code, String message, T data) {

    public static <T> HmsResponseBody<T> success(T data) {
        return new HmsResponseBody<>(0, null, data);
    }

    public static <T> HmsResponseBody<T> success() {
        return HmsResponseBody.success(null);
    }

    public static <T> HmsResponseBody<T> error(ErrorCode errorCode, T data) {
        return new HmsResponseBody<>(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static <T> HmsResponseBody<T> error(ErrorCode errorCode) {
        return HmsResponseBody.error(errorCode, null);
    }

    public static <T> HmsResponseBody<T> error(int code, String message, T data) {
        return new HmsResponseBody<>(code, message, data);
    }

    public static <T> HmsResponseBody<T> error(int code, String message) {
        return new HmsResponseBody<>(code, message, null);
    }
}
