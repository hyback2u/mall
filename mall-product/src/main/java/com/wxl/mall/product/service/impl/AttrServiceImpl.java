package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.AttrAttrgroupRelationDao;
import com.wxl.mall.product.dao.AttrDao;
import com.wxl.mall.product.dao.AttrGroupDao;
import com.wxl.mall.product.dao.CategoryDao;
import com.wxl.mall.product.entity.AttrAttrgroupRelationEntity;
import com.wxl.mall.product.entity.AttrEntity;
import com.wxl.mall.product.entity.AttrGroupEntity;
import com.wxl.mall.product.entity.CategoryEntity;
import com.wxl.mall.product.service.AttrService;
import com.wxl.mall.product.service.CategoryService;
import com.wxl.mall.product.vo.AttrResponseVO;
import com.wxl.mall.product.vo.AttrVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Resource
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Resource
    private AttrGroupDao attrGroupDao;

    @Resource
    private CategoryDao categoryDao;

    @Resource
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), new QueryWrapper<>());

        return new PageUtils(page);
    }

    /**
     * 保存, 这里入参修改为VO, 为了能够多收集一些特定的需要的信息
     *
     * @param attr AttrVO
     */
    @Override
    @Transactional
    public void saveAttrVO(AttrVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        // 这样搞, 麻烦的一批... --> BeanUtils, 前提是属性名一一对应的
//        attrEntity.setAttrName(attr.getAttrName());
        BeanUtils.copyProperties(attr, attrEntity);

        // 1、向attr表中, 保存基础信息
        this.save(attrEntity);

        // 2、保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());

        attrAttrgroupRelationDao.insert(relationEntity);
    }

    /**
     * 获取分类下的规格参数功能
     *
     * @param params    封装的查询参数
     * @param catelogId 分类id
     * @return PageUtils
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        // 如果检索条件存在, 就拼进去
        if (StringUtils.isNotBlank(key)) {
            wrapper.eq("attr_id", key).or().like("attr_name", key).like("attr_type", key);
        }

        // 根据不同情况封装不同条件
        if (!catelogId.equals(0L)) {
            wrapper.and((x) -> x.eq("catelog_id", catelogId));
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        // -------> 组装VO
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVO> attrResponseVOList = records.stream().map(attrEntity -> {
            AttrResponseVO attrRespVO = new AttrResponseVO();
            // 1、设置基本数据
            BeanUtils.copyProperties(attrEntity, attrRespVO);
            // 2、设置分类和分组的名字
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (null != relationEntity && null != relationEntity.getAttrGroupId()) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                attrRespVO.setGroupName(attrGroupEntity.getAttrGroupName());
            }

            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (null != categoryEntity) {
                attrRespVO.setCatelogName(categoryEntity.getName());
            }

            return attrRespVO;
        }).collect(Collectors.toList());

        // 替换处理后的数据
        pageUtils.setList(attrResponseVOList);

        return pageUtils;
    }

    /**
     * 根据属性实体id, 查询基础信息后组装所在分类全路径
     *
     * @param attrId 属性实体id
     * @return 关联一些其它信息后以AttrResponseVO返回
     */
    @Override
    public AttrResponseVO getAttrInfo(Long attrId) {
        AttrResponseVO attrRespVO = new AttrResponseVO();
        // 1、基础信息
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVO);

        // 2、AttrResponseVO塞值: (1):分组信息 (2)所属分类全路径
        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        // (1):设置分组信息
        if (Objects.nonNull(relationEntity)) {
            attrRespVO.setAttrGroupId(relationEntity.getAttrGroupId());
            // 当前分组详细信息
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
            if (Objects.nonNull(attrGroupEntity)) {
                attrRespVO.setAttrName(attrGroupEntity.getAttrGroupName());
            }
        }

        // (2)所属分类全路径
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPathByCatelogId(catelogId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (Objects.nonNull(categoryEntity)) {
            attrRespVO.setCatelogPath(catelogPath);
        }
        attrRespVO.setCatelogName(categoryEntity.getName());

        return attrRespVO;
    }

}
