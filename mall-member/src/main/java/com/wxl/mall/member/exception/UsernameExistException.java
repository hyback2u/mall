package com.wxl.mall.member.exception;

/**
 * @author wangxl
 * @since 2022/6/26 18:05
 */
public class UsernameExistException extends RuntimeException {
    public UsernameExistException() {
        super("用户名存在");
    }
}
