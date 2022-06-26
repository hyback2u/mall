package com.wxl.mall.auth.feign;

import com.wxl.common.utils.R;
import com.wxl.mall.auth.vo.UserLoginVO;
import com.wxl.mall.auth.vo.UserRegisterVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 对会员服务的远程调用
 *
 * @author wangxl
 * @since 2022/6/26 19:00
 */
@FeignClient("mall-member")
public interface MemberFeignService {

    /**
     * 注册
     *
     * @param vo 会员注册VO
     * @return R
     */
    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterVO vo);


    /**
     * 登录功能
     *
     * @param vo MemberLoginVO
     * @return R
     */
    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVO vo);
}
