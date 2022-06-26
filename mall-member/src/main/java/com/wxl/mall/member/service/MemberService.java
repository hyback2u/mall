package com.wxl.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.member.entity.MemberEntity;
import com.wxl.mall.member.exception.PhoneExistException;
import com.wxl.mall.member.exception.UsernameExistException;
import com.wxl.mall.member.vo.MemberRegisterVO;

import java.util.Map;

/**
 * 会员
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:13:25
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 注册
     *
     * @param vo vo
     */
    void register(MemberRegisterVO vo);


    /**
     * 检查手机号是否唯一
     *
     * @param phone 手机号
     */
    void checkPhoneUnique(String phone) throws PhoneExistException;

    /**
     * 检查用户名是否唯一
     *
     * @param username 用户名
     */
    void checkUsernameUnique(String username) throws UsernameExistException;
}

