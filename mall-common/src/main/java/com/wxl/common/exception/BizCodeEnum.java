package com.wxl.common.exception;

/**
 * 错误信息业务状态码
 *
 * @author wangxl
 * @since 2022/5/28 14:51
 */
public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VALID_EXCEPTION(10001, "参数格式校验失败"),
    SMS_CODE_EXCEPTION(10002, "验证码获取频率太高, 请稍后再试"),
    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),
    USERNAME_EXIST_EXCEPTION(15001, "用户名已存在"),
    PHONE_EXIST_EXCEPTION(15002, "手机号已被注册"),
    LOGIN_ACCOUNT_PASSWORD_INVALID_EXCEPTION(15003, "用户名或密码错误");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
