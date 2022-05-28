package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.AttrGroupDao;
import com.wxl.mall.product.entity.AttrGroupEntity;
import com.wxl.mall.product.service.AttrGroupService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 如果没有三级分类, 就传0, 查询所有; 有的话就查三级分类下的
     *
     * @param params     分页参数
     * @param categoryId 三级分类id
     * @return message&data
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        // todo 这里分页的逻辑需要了解下
        if (categoryId.equals(0L)) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<>());
            return new PageUtils(page);
        }

        // 构造查询条件
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", categoryId);

        // 提交的检索条件是否存在, 检索条件存在, 则进行多字段模糊匹配查询
        String searchKey = (String) params.get("key");
        if (StringUtils.isNotBlank(searchKey)) {
            // 继续构造
            wrapper.and((obj) -> {
                obj.like("attr_group_id", searchKey)
                        .or().like("attr_group_name", searchKey)
                        .or().like("descript", searchKey);
            });
        }

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

}
