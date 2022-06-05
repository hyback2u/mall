package com.wxl.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.constant.ProductConstant;
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
import com.wxl.mall.product.vo.AttrGroupRelationVO;
import com.wxl.mall.product.vo.AttrResponseVO;
import com.wxl.mall.product.vo.AttrVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
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

        // 2、保存关联关系, fixed这里如果当前页面提交的attrVO里的类型是销售属性, 下面就不需要做了
//        if (!attr.getAttrType().equals(1)) {  todo:这里保持好习惯, 不使用魔法值~
        // 0602: fixme fixed, just note:这里, 因为保存的时候, 所属分组不是必填的, 所以关系表保存与否要判断
        if (attr.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) &&
                null != attr.getAttrGroupId()) {
            // 如果是销售属性, 就此终止
            return;
        }

        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());

        attrAttrgroupRelationDao.insert(relationEntity);
    }

    /**
     * 获取分类下的规格参数功能
     *
     * @param params    封装的查询参数
     * @param attrType  0-销售属性，1-基本属性
     * @param catelogId 分类id
     * @return PageUtils
     */
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, String attrType, Long catelogId) {
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

        // 基本属性 or 销售属性
        wrapper.and((x) -> x.eq("attr_type", "base".equalsIgnoreCase(attrType) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()));

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);

        // -------> 组装VO
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVO> attrResponseVOList = records.stream().map(attrEntity -> {
            AttrResponseVO attrRespVO = new AttrResponseVO();
            // 1、设置基本数据
            BeanUtils.copyProperties(attrEntity, attrRespVO);
            // 2、设置分类和分组的名字 fixme:销售属性查询是不需要设置分组信息的, 这里是根据非空判断的
            // todo fixme 这里, 有bug
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao
                    .selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
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


        log.info("*************AttrServiceImpl.queryBaseAttrPage, attrVOList = {}", JSON.toJSONString(attrResponseVOList));

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
        // (1):设置分组信息 fixed:销售属性不需要
        if (attrEntity.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())) {
            if (Objects.nonNull(relationEntity)) {
                attrRespVO.setAttrGroupId(relationEntity.getAttrGroupId());
                // 当前分组详细信息
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (Objects.nonNull(attrGroupEntity)) {
                    attrRespVO.setGroupName(attrGroupEntity.getAttrGroupName());
                }
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

    /**
     * 修改
     *
     * @param attrResponseVO attrResponseVO
     */
    @Override
    @Transactional
    public void updateAttr(AttrResponseVO attrResponseVO) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrResponseVO, attrEntity);
        // 1、修改基本信息
        this.updateById(attrEntity);

        // fixed: 如果是销售属性, 就此终止
        if (attrResponseVO.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode())) {
            return;
        }

        // 2、修改/新增 分组关联(针对于本来就没有的记录的操作优化)
        Integer count = attrAttrgroupRelationDao.selectCount(new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrResponseVO.getAttrId()));

        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrResponseVO.getAttrGroupId());
        relationEntity.setAttrId(attrResponseVO.getAttrId());

        if (count > 0) {
            // 修改
            attrAttrgroupRelationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrResponseVO.getAttrId()));
        } else {
            // 新增
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }

    /**
     * 根据分组id, 找到组内所有的属性实体信息
     *
     * @param attrgroupId 属性分组id
     * @return AttrEntityList
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        List<Long> ids = entities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        // fixme:ids有可能是空的, 需要做非空判断
        if (ids.size() == 0) {
            return null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(ids);

        return (List<AttrEntity>) attrEntities;
    }

    /**
     * 删除属性与分组的关联关系
     *
     * @param vos vos 数组, 提交过来的是多个值(因为, 既有单个, 也有批量, 批量兼容单个)
     */
    @Override
    public void deleteRelation(AttrGroupRelationVO[] vos) {
        // 这样写会发送很多次删除请求 todo why？
//        attrAttrgroupRelationDao.delete(new QueryWrapper<AttrAttrgroupRelationEntity>()
//                .eq("attr_id", 1L)
//                .eq("attr_group_id", 1L));

        // 希望只发送一次请求 todo:这里与老师不一样也可以呀...
        List<AttrAttrgroupRelationEntity> relationEntities = Arrays.stream(vos).map(item -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        attrAttrgroupRelationDao.deleteBatchRelation(relationEntities);
    }

    /**
     * 展示数据：当前分组, 可关联的所有属性
     * 即：肯定是本分类下的, 而且是本分类下没有被其它分组关联的属性
     *
     * @param params      pageParams
     * @param attrgroupId 当前分组id
     * @return 当前分组, 可关联的所有属性
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        Long catelogId = attrGroupDao.selectById(attrgroupId).getCatelogId();

        List<AttrAttrgroupRelationEntity> relationEntityList = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrgroupId));

        // 1.本分类下的所有的属性, 构造初始化全量基本属性wrapper
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId)
                .eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
//        List<AttrEntity> attrEntityList = this.list(new QueryWrapper<AttrEntity>()
//                .eq("catelog_id", catelogId));

        // 去除掉已经被当前引用的属性ids
        List<Long> usedIds = relationEntityList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        if (usedIds.size() > 0) {
            queryWrapper.and((wrapper) -> wrapper.notIn("attr_id", usedIds));
        }

        // 2.1 本分类下的所有分组(除去自己)
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId).ne("attr_group_id", attrgroupId));
        // 2.2 本分类下除去自己的所有分组 所关联的所有属性
        // noteL:这里, 分类下有其它分组, 再对全量属性进行过滤
        if (null != attrGroupEntities && attrGroupEntities.size() > 0) {
            List<Long> groupIds = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
            List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .in("attr_group_id", groupIds));
            List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
            if (attrIds.size() > 0) {
                queryWrapper.and((wrapper) -> wrapper.notIn("attr_id", attrIds));
            }
        }


        // 3.页面如果有模糊查询
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            queryWrapper.and((wrapper) -> wrapper.eq("attr_id", key).or().like("attr_name", key));
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);

        return new PageUtils(page);
    }


    /**
     * 在指定的所有属性集合中, 挑出检索属性
     *
     * @param attrIds attrIds
     * @return attrIds_Searchable
     */
    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {
        // select attr_id from `pms_attr` where attr_id in(?) and search_type=1
        return this.baseMapper.selectSearchAttrIds(attrIds);
    }

//    /**
//     * 获取属性分组没有关联的其他属性
//     *
//     * @param params      分页参数
//     * @param attrgroupId 属性分组id
//     * @return page
//     */
//    @Override
//    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
//        // 1、当前分组, 只能关联自己所属分类里面的所有属性
//        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
//        Long catelogId = attrGroupEntity.getCatelogId();
//        // 多余了是吧...
////        List<AttrEntity> attrEntities = this.list(new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId));
//
//        // 2、当前分组, 只能关联别的属性没有引用的属性
//        // 2.1 当前分类下的其它分组(还不能包含当前分组)
//        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId).ne("attr_group_id", attrgroupId));
//        List<Long> groupIds = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
//        // 2.2 这些分组关联的属性
//        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", groupIds));
//        List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
//
//        // 2.3 从当前分类的所有属性中移除这些属性(这里我是想要在内存中处理来着...得, 避免不了, 这样刚开始就查了)
//        // fixme:notIn/in操作, 空集合会报错, 所以拼装前需要判断
//        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
////                .eq("catelog_id", catelogId).notIn("attr_ids", attrIds);
//                .eq("catelog_id", catelogId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
//        if (attrIds.size() > 0) {
//            queryWrapper.and((x) -> x.notIn("attr_ids", attrIds));
//        }
//
//        // 页面如果有模糊查询
//        String key = (String) params.get("key");
//        if (StringUtils.isNotBlank(key)) {
//            queryWrapper.and((wrapper) -> wrapper.eq("attr_id", key).or().like("attr_name", key));
//        }
//
//        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
//
//        return new PageUtils(page);
//    }

}
