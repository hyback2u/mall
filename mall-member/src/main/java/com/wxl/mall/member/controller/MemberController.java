package com.wxl.mall.member.controller;

import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.R;
import com.wxl.mall.member.entity.MemberEntity;
import com.wxl.mall.member.feign.CouponFeignService;
import com.wxl.mall.member.service.MemberService;
import com.wxl.mall.member.vo.MemberRegisterVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 会员
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:13:25
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Resource
    private MemberService memberService;

    @Resource
    private CouponFeignService couponFeignService;


    /**
     * 注册
     *
     * @param vo 会员注册VO
     * @return R
     */
    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVO vo) {
        try {
            memberService.checkPhoneUnique(vo.getPhone());
            memberService.checkUsernameUnique(vo.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        memberService.register(vo);

        return R.ok();
    }

    /**
     * 获取当前会员的所有优惠券
     *
     * @return 所有优惠券
     */
    @RequestMapping(value = "/coupons")
    public R feignTest() {
        // 模拟会员信息从数据库中查的
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");

        R memberCoupons = couponFeignService.memberCoupons();

        return R.ok()
                .put("member", memberEntity)
                .put("coupons", memberCoupons.get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
