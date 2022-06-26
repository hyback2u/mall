package com.wxl.mall.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 登录页面VO
 *
 * @author wangxl
 * @since 2022/6/26 20:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLoginVO {
    private String loginAccount;
    private String password;
}
