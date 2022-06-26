package com.wxl.mall.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 用户注册页面表单数据VO(使用JSR303校验)
 *
 * @author wangxl
 * @since 2022/6/26 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisterVO {
    @NotEmpty(message = "用户名必须提交")
    private String userName;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 18, message = "密码必须大于6位")
    private String password;

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式不正确")
    private String phone;

    @NotEmpty(message = "验证码不能为空")
    private String code;
}
