package me.huangduo.hms.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HmsHttpStatusEnum {
    BUSINESS_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "业务异常"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "错误的请求"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "未认证的用户"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "拒绝访问"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "请求的资源未找到"),
    CONFLICT(HttpStatus.CONFLICT, "请求冲突"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "服务出错, 请稍后再试"),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "网关错误, 请稍后再试"),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "请求的内容类型不被接受"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "请求方法不支持"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "不支持的媒体类型");


    private final HttpStatus code;

    private final String message;

    HmsHttpStatusEnum(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageByHttpCode(int statusCode) {
        for (HmsHttpStatusEnum status : HmsHttpStatusEnum.values()) {
            if (status.getCode().value() == statusCode) {
                return status.getMessage();
            }
        }
        return null;
    }
}