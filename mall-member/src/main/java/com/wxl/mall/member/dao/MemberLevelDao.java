package com.wxl.mall.member.dao;

import com.wxl.mall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:13:25
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {

    /**
     * 获取默认的会员等级
     *
     * @return 会员等级实体类
     */
    MemberLevelEntity getDefaultLevel();
}
