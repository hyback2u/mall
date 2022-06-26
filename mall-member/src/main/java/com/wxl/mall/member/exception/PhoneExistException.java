package com.wxl.mall.member.exception;

/**
 * @author wangxl
 * @since 2022/6/26 18:06
 */
public class PhoneExistException extends RuntimeException {

    public PhoneExistException() {
        super("手机号已存在");
    }
}
