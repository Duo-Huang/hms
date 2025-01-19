package me.huangduo.hms.dto.response;

import me.huangduo.hms.enums.HmsErrorCodeEnum;

public record HmsResponse<T>(int code, String message, T data) {

    public static <T> HmsResponse<T> success(T data) {
        return new HmsResponse<>(0, null, data);
    }

    public static <T> HmsResponse<T> success() {
        return HmsResponse.success(null);
    }

    public static <T> HmsResponse<T> error(HmsErrorCodeEnum hmsErrorCodeEnum, T data) {
        return new HmsResponse<>(hmsErrorCodeEnum.getCode(), hmsErrorCodeEnum.getMessage(), data);
    }

    public static <T> HmsResponse<T> error(HmsErrorCodeEnum hmsErrorCodeEnum) {
        return HmsResponse.error(hmsErrorCodeEnum, null);
    }

    public static <T> HmsResponse<T> error(int code, String message, T data) {
        return new HmsResponse<>(code, message, data);
    }
}
