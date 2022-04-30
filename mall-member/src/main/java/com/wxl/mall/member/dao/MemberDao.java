package com.wxl.mall.member.dao;

import com.wxl.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:13:25
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
