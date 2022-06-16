package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.AttrGroupDao;
import com.wxl.mall.product.entity.AttrEntity;
import com.wxl.mall.product.entity.AttrGroupEntity;
import com.wxl.mall.product.service.AttrGroupService;
import com.wxl.mall.product.service.AttrService;
import com.wxl.mall.product.vo.AttrGroupWithAttrsVO;
import com.wxl.mall.product.vo.SpuItemAttrGroupVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // todo 这里分页的逻辑需要了解下
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<>()
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
        // 构造查询条件
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();

        // 提交的检索条件是否存在, 检索条件存在, 则进行多字段模糊匹配查询
        String searchKey = (String) params.get("key");
        if (StringUtils.isNotBlank(searchKey)) {
            // 继续构造
            wrapper.and((obj) -> obj.eq("attr_group_id", searchKey)
                    .or().like("attr_group_name", searchKey)
                    .or().like("descript", searchKey));
        }

        if (categoryId.equals(0L)) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }

        wrapper.eq("catelog_id", categoryId);

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }


    /**
     * 根据分类id查出所有的分组, 以及这些组里面的属性
     *
     * @param catelogId 三级分类id
     * @return data -> VO
     */
    @Override
    public List<AttrGroupWithAttrsVO> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        // 1、查出当前分类下的所有属性分组
        List<AttrGroupEntity> attrGroupEntityList = this.list(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));

        // fixme:这里的话, for循环调用dao访问db？性能呢？
        // 2、查出每个分组下的所有属性
        return attrGroupEntityList.stream().map(group -> {
            AttrGroupWithAttrsVO vo = new AttrGroupWithAttrsVO();
            // 1.复制基本数据
            BeanUtils.copyProperties(group, vo);

            // 2.属性
            List<AttrEntity> attrs = attrService.getRelationAttr(vo.getAttrGroupId());
            vo.setAttrs(attrs);

            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 查出当前Spu对应的所有属性的分组信息, 以及当前分组下的所有属性对应的值
     *
     * @param spuId     spuId
     * @param catalogId 三级分类id
     * @return data -> VO
     */
    @Override
    public List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        AttrGroupDao baseMapper = this.getBaseMapper();
        return baseMapper.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
    }

}
