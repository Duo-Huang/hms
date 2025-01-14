package me.huangduo.hms.dto.response;

public record HmsResponse<T>(int code, String message, T data) {

    public static <T> HmsResponse<T> success() {
        return new HmsResponse<>(0, null, null);
    }

    public static <T> HmsResponse<T> success(T data) {
        return new HmsResponse<>(0, null, data);
    }

    public static <T> HmsResponse<T> error(int code, String message) {
        return new HmsResponse<>(code, message, null);
    }

    public static <T> HmsResponse<T> error(int code, String message, T data) {
        return new HmsResponse<>(code, message, data);
    }
}
