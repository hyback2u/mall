package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.CategoryDao;
import com.wxl.mall.product.entity.CategoryEntity;
import com.wxl.mall.product.service.CategoryBrandRelationService;
import com.wxl.mall.product.service.CategoryService;
import com.wxl.mall.product.vo.Catelog2VO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    // 这是最原始的写法, 但是因为已经继承了ServiceImpl, 且该实现加入了泛型CategoryDao的实现
//    @Resource
//    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(new Query<CategoryEntity>().getPage(params), new QueryWrapper<>());

        return new PageUtils(page);
    }

    /**
     * 查出所有分类以及子分类, 以树形结构组装起来
     *
     * @return List<CategoryEntity>
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        // 1、查出所有分类(没有查询条件, 传入null, 就是查询所有)
        List<CategoryEntity> categoryEntityList = baseMapper.selectList(null);

        // 2、组装成父子树形结构
        List<CategoryEntity> entityList = categoryEntityList.stream()
                // 1、找到所有的一级分类(一级分类特点:父分类id: parent_cid是0)
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(0L))
                // 2、collect收集之前, 改变菜单里的一些属性, 这里通过map将每一个菜单里的内容改变一下 map->peek, 少了一个return
                .peek(categoryEntity -> {
                    // 将当前菜单的子分类保存进去
                    categoryEntity.setChildren(getChildCategoryEntityList(categoryEntity, categoryEntityList));
                })
                // 3、排序: (menu1, menu2) -> {return menu1.getSort() - menu2.getSort(); }
                // fixed.NPE 优化前:.sorted((menu1, menu2) -> (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort()))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());

        log.info("**********************categoryList size: {}", entityList.size());

        return entityList;
    }

    /**
     * @param idList 要删除的id的集合, 批量删除(这里考虑以后在写实现时, 都是批量的, 当然兼容单个)
     */
    @Override
    public void removeConditionalByIds(List<Long> idList) {
        // 1、执行删除前, 先执行检查 TODO 并不知道业务中什么会引用逻辑菜单

        // 这里就是批量删除的最终执行语句, 不过这个是物理删除(删了以后就真没了)
        // 逻辑删除:只是使用我们某一个字段作为标识位, 来表示是否被删除, 如pms_category表里的show_status(1显示, 0不显示)
        baseMapper.deleteBatchIds(idList);
    }

    /**
     * 根据三级分类id查询出该分类的完整路径
     *
     * @param catelogId 三级分类id
     * @return Long[], 分类的完整路径, eg:[2, 25, 230]
     */
    @Override
    public Long[] findCatelogPathByCatelogId(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        // 得到的需要进行逆序转换
        Collections.reverse(parentPath);

        Long[] catelogPath = new Long[parentPath.size()];
        return parentPath.toArray(catelogPath);
    }

    /**
     * 在更新分类实例的同时, 更新关联的所有的冗余表, 保证冗余字段的数据一致
     * 这里需要开启MyBatisPlus中的事务注解, 这里才能使用事务
     *
     * @param category 分类实例
     * @see com.wxl.mall.product.config.MyBatisPlusConfig 开启事务
     */
    @Override
    @Transactional
    public void updateCascade(CategoryEntity category) {
        // 1、更新自身的表
        this.updateById(category);

        // 2、如果分类名修改了, 数据同步更新关联的冗余表
        if (StringUtils.isBlank(category.getName())) {
            return;
        }

        // 数据同步更新关联的冗余表
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

        // todo 其它要更新的关联表...
    }


    /**
     * 一级分类数据
     *
     * @return data
     */
    @Override
    public List<CategoryEntity> getLevel1Categories() {
        return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>()
                .eq("parent_cid", 0));
    }


    /**
     * [前端]查出所有分类, 按照形式组织后返回
     *
     * @return data
     */
    @Override
    public Map<String, List<Catelog2VO>> getCatalogJson() {
        // 1、查出所有一级分类
        List<CategoryEntity> level1Categories = this.getLevel1Categories();

        // 2、封装数据
        Map<String, List<Catelog2VO>> map = level1Categories.stream().collect(Collectors.toMap(
                k -> k.getCatId().toString(),
                v -> {
                    // 每一个的一级分类, 查到这个一级分类的二级分类
                    List<CategoryEntity> categoryEntityList = baseMapper.selectList(new QueryWrapper<CategoryEntity>()
                            .eq("parent_cid", v.getCatId()));

                    List<Catelog2VO> catelog2VOList = new ArrayList<>();
                    if (null != categoryEntityList) {
                        catelog2VOList = categoryEntityList.stream().map(item -> {
                                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, item.getCatId().toString(), item.getName());

                                    // 找到当前二级分类的三级分类封装成VO
                                    List<CategoryEntity> entities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", catelog2VO.getId()));
                                    List<Catelog2VO.Catelog3VO> catelog3VOList = new ArrayList<>();
                                    if (null != entities) {
                                        catelog3VOList = entities.stream().map(x ->
                                                new Catelog2VO.Catelog3VO(item.getCatId().toString(), x.getCatId().toString(), x.getName())
                                        ).collect(Collectors.toList());
                                    }
                                    catelog2VO.setCatalog3List(catelog3VOList);

                                    return catelog2VO;
                                })
                                .collect(Collectors.toList());
                    }
                    return catelog2VOList;
                }
        ));

        log.info("**********map = {}", map);

        return map;
    }

    /**
     * 根据三级分类id查询出该分类的完整路径
     *
     * @param catelogId 三级分类id
     * @param paths     收集用
     * @return eg:[230, 25, 2], 这里收集到的时逆序的
     */
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        // 1、收集当前节点id
        paths.add(catelogId);

        CategoryEntity category = this.getById(catelogId);
        if (!Objects.equals(category.getParentCid(), 0L)) {
            // 2、父节点id
            findParentPath(category.getParentCid(), paths);
        }

        return paths;
    }


    /**
     * 递归的方法找到每一个菜单的子菜单
     *
     * @param root 当前菜单
     * @param all  要从哪里获取它的子菜单
     * @return 当前菜单的子菜单
     */
    private List<CategoryEntity> getChildCategoryEntityList(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream()
                // 1、如果目标菜单的父节点=root节点的id, 那么目标菜单就是当前菜单的子菜单, 就归属于root旗下了
                // fixme: 优化前:.filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId())
                .filter(categoryEntity -> Objects.equals(categoryEntity.getParentCid(), root.getCatId()))
                // 2、子菜单还有可能有子菜单, 这里为每一个菜单找到它的子菜单(同样是map->peek)
                .peek(categoryEntity -> categoryEntity.setChildren(getChildCategoryEntityList(categoryEntity, all)))
                // 3、排序
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());
    }

}
