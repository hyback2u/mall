package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.AttrAttrgroupRelationDao;
import com.wxl.mall.product.entity.AttrAttrgroupRelationEntity;
import com.wxl.mall.product.service.AttrAttrgroupRelationService;
import com.wxl.mall.product.vo.AttrGroupRelationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(new Query<AttrAttrgroupRelationEntity>().getPage(params), new QueryWrapper<>());

        return new PageUtils(page);
    }


    /**
     * 添加属性与分组关联关系
     *
     * @param vos 属性id,属性分组id
     */
    @Override
    public void saveBatchRelation(List<AttrGroupRelationVO> vos) {
        // 1.属性对拷, 转换对象
        List<AttrAttrgroupRelationEntity> relationEntityList = vos.stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);

            return relationEntity;
        }).collect(Collectors.toList());

        // 2.调用保存方法
        this.saveBatch(relationEntityList);
    }

}
