package me.huangduo.hms.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HmsHttpStatusEnum {
    BUSINESS_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "业务异常"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "错误的请求"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "未认证的用户"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "拒绝访问"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "请求找不到"),
    CONFLICT(HttpStatus.CONFLICT, "请求冲突"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "服务出错, 请稍后再试"),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "网关错误, 请稍后再试");

    private final HttpStatus code;

    private final String message;

    HmsHttpStatusEnum(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }
}
