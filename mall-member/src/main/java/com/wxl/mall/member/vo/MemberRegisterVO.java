package com.wxl.mall.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 会员注册VO
 *
 * @author wangxl
 * @since 2022/6/26 17:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberRegisterVO {
    private String userName;
    private String password;
    private String phone;
}
