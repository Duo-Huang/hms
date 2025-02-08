package me.huangduo.hms.enums;

import lombok.Getter;

/**
 * 业务错误统一使用422 http status
 * 统一定义错误码[response.data.code]
 * 错误码规范
 * 统一在一个文件中定义错误码；
 * 错误码长度为 4 位；
 * 第 1 位表示错误是哪种级别？例如：1 为系统级错误，2 为业务模块错误，可标记 9 种错误级别。
 * 第 2 位表示错误是哪个业务模块？例如：1 为用户模块，2 为家庭模块，3 为财务模块, 可标记 9 个业务模块。
 * 第 3 位和第 4 位表示具体是什么错误？例如：01 为手机号不合法，02 为验证码输入错误，可标记 99 个错误。
 */
@Getter
public enum ErrorCodeEnum {

    // 系统级错误 1xxx (发生在全局范围内或者不确定具体地方)
    SYSTEM_ERROR_001(1001, "未知错误"), // fallback
    SYSTEM_ERROR_002(1002, "请求出错了"), // 处理所有未被全局处理器处理的异常
    SYSTEM_ERROR_003(1003, "请求参数错误"), // 全局参数校验的fallback消息
    SYSTEM_ERROR_004(1004, "业务异常"), // 具体异常api内处理, 这里是全局兜底的错误

    // 用户模块错误 21xx
    USER_ERROR_101(2101, "用户未认证"),
    USER_ERROR_102(2102, "此用户无权限, 请联系家庭管理员"),
    USER_ERROR_103(2103, "用户已存在"),
    USER_ERROR_104(2104, "用户名或密码格式错误"), // 注册
    USER_ERROR_105(2105, "用户名或密码错误"), // 登录
    USER_ERROR_106(2106, "用户信息格式错误"),
    USER_ERROR_107(2107, "更新密码信息错误"),
    USER_ERROR_108(2108, "密码错误"), // change password
    USER_ERROR_109(2109, "新密码不能和旧密码相同"),

    // 家庭模块错误 22xx
    HOME_ERROR_201(2201, "该家庭已存在"),
    HOME_ERROR_202(2202, "家庭信息格式错误"),
    HOME_ERROR_203(2203, "该家庭不存在"),
    HOME_ERROR_204(2204, "该用户不存在"),
    HOME_ERROR_205(2205, "该家庭成员已存在"),
    HOME_ERROR_206(2206, "该家庭成员不存在"),
    HOME_ERROR_207(2207, "家庭成员信息格式错误"),
    HOME_ERROR_208(2208, "家庭管理员角色不存在"),
    HOME_ERROR_209(2219, "用户名格式错误"),
    HOME_ERROR_2010(2210, "该角色已存在"),
    HOME_ERROR_2011(2211, "该家庭中无此角色"),
    HOME_ERROR_2012(2212, "角色信息格式错误"),
    HOME_ERROR_2013(2213, "该用户无权访问此家庭"),
    HOME_ERROR_2014(2214, "权限信息格式错误"),
    HOME_ERROR_2015(2215, "邀请码格式错误"),
    HOME_ERROR_2016(2216, "邀请信息不存在"),
    HOME_ERROR_2017(2217, "邀请已过期"),
    HOME_ERROR_2018(2218, "家庭成员角色不存在"),

    // 财务模块错误 23xx
    FINANCE_ERROR_301(2301, "预算不足");

    private final int code;
    private final String message;

    ErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
