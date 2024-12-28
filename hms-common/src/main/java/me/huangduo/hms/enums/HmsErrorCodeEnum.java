package me.huangduo.hms.enums;

import lombok.Getter;

/**
 * 业务错误统一使用422 http status
 * 统一定义错误码[response.data.code]
 * 错误码规范
 * 统一在一个文件中定义错误码；
 * 错误码长度为 4 位；
 * 第 1 位表示错误是哪种级别？例如：1 为系统级错误，2 为业务模块错误，可标记 9 种错误级别。
 * 第 2 位表示错误是哪个模块？例如：1 为用户模块，2 为财务模块，可标记 9 个模块。
 * 第 3 位和第 4 位表示具体是什么错误？例如：01 为手机号不合法，02 为验证码输入错误，可标记 99 个错误。
 */
@Getter
public enum HmsErrorCodeEnum {

    // 系统级错误(发生在全局范围内或者不确定具体地方)
    SYSTEM_ERROR_001(1001, "系统内部错误"),
    SYSTEM_ERROR_002(1002, "数据库连接失败"),
    SYSTEM_ERROR_003(1003, "请求参数错误"), // 全局参数校验的fallback消息

    // 用户模块错误
    USER_ERROR_101(2101, "用户未认证"),
    USER_ERROR_102(2102, "此用户无权限, 请联系家庭管理员"),
    USER_ERROR_103(2103, "用户已存在, 请直接登录"),
    USER_ERROR_104(2104, "用户未找到"),
    USER_ERROR_105(2105, "用户名或密码格式错误"), // 注册
    USER_ERROR_106(2106, "用户名或密码错误"), // 登录

    // 财务模块错误
    FINANCE_ERROR_201(2201, "预算不足");

    private final int code;
    private final String message;

    HmsErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
