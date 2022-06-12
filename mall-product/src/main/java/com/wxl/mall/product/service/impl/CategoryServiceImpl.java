package com.wxl.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

//    /**
//     * 最简单的缓存技术, Map
//     */
//    private Map<String, Object> cache = new HashMap<>();

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
        return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
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
        Map<String, List<Catelog2VO>> map = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 每一个的一级分类, 查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntityList = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));

            List<Catelog2VO> catelog2VOList = new ArrayList<>();
            if (null != categoryEntityList) {
                catelog2VOList = categoryEntityList.stream().map(item -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, item.getCatId().toString(), item.getName());

                    // 找到当前二级分类的三级分类封装成VO
                    List<CategoryEntity> entities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", catelog2VO.getId()));
                    List<Catelog2VO.Catelog3VO> catelog3VOList = new ArrayList<>();
                    if (null != entities) {
                        catelog3VOList = entities.stream().map(x -> new Catelog2VO.Catelog3VO(item.getCatId().toString(), x.getCatId().toString(), x.getName())).collect(Collectors.toList());
                    }
                    catelog2VO.setCatalog3List(catelog3VOList);

                    return catelog2VO;
                }).collect(Collectors.toList());
            }
            return catelog2VOList;
        }));

        log.info("**********map = {}", map);

        return map;
    }


    /**
     * [前端]查出所有分类, 按照形式组织后返回
     * 优化项:将数据库的多次查询变为一次
     *
     * @return data
     * @see CategoryServiceImpl#getCatalogDataFromDB()
     */
    @Override
    public Map<String, List<Catelog2VO>> getCatalogJsonPlus() {
        // 说明:这块为了代码后续方法review以及见名知意的复用, 这里将原先逻辑抽取为getCatalogDataFromDB()方法
        return getCatalogDataFromDB();
    }

    /**
     * [前端]查出所有分类, 按照形式组织后返回
     * -----------------------------------
     * 优化项: 引入redis缓存中间件
     * <p>
     * -------------------------------------------------------------------------------------
     * 说明:给缓存中放json字符串, 拿出的json字符串, 还要逆转为能用的对象类型 --> 序列化 & 反序列化
     * -------------------------------------------------------------------------------------
     * 由于网络传输, 要存到远程的redis里面, 肯定要整成一个可传输的流(串)数据, 即:序列化与反序列化
     * <p>
     * fixme:堆外内存溢出 OutOfDirectMemoryError
     *
     * @return data
     */
    @Override
    public Map<String, List<Catelog2VO>> getCatalogJsonPlusPro() {
        // 1、加入缓存逻辑(缓存中存的数据都是Json字符串, Json的好处就是, 跨语言、跨平台兼容的)
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        // 2、缓存中有, 返回缓存内容
        if (StringUtils.isNotEmpty(catalogJson)) {
            log.info("********redis log, 命中缓存, 返回缓存内容");
            // 拿出的字符串, 逆转为能用的对象类型
            return JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2VO>>>() {
            });
        }

        // 3、缓存中没有, 查询数据库
        log.info("********redis log, 从数据库中获取返回");
//        Map<String, List<Catelog2VO>> catalogJsonFromDB = getCatalogDataFromDB();
        // 采用锁
//        Map<String, List<Catelog2VO>> catalogJsonFromDB = getCatalogDataWithRedisLock();
        Map<String, List<Catelog2VO>> catalogJsonFromDB = getCatalogDataWithRedisson();

        // 3.1 查到的数据放入缓存, 将对象转为Json放入缓存 --> 本地锁的情况下, 这块内容需要同步到锁里面
//        stringRedisTemplate.opsForValue().set("catalogJson", JSON.toJSONString(catalogJsonFromDB));
        log.info("******************************************************************");

        return catalogJsonFromDB;
    }


    // ------------------------------------------------------------------------------------------------------

    /**
     * Redisson Lock
     *
     * 缓存里的数据, 如何和数据库里的数据保持一直一致？ 缓存数据一致性问题
     * 1、双写模式
     * 2、失效模式
     */
    public Map<String, List<Catelog2VO>> getCatalogDataWithRedisson() {
        // 注意锁的名字, 锁的粒度越细越快
        // 锁的粒度: 具体缓存的是某个数据, eg:11号商品 product-11-lock
        RLock lock = redissonClient.getLock("catalogJson-lock");
        lock.lock();

        Map<String, List<Catelog2VO>> data;
        try {
            data = getCatalogDataFromDBAndPutCache();
        } finally {
            lock.unlock();
        }

        return data;
    }



    /**
     * 使用Redis分布式锁
     *
     * @see CategoryServiceImpl#getCatalogDataWithRedisson()
     */
    @Deprecated
    public Map<String, List<Catelog2VO>> getCatalogDataWithRedisLock() {
        // 1、占分布式锁, 去redis占坑 + 设置过期时间(原子操作)
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);

        // todo:这里为啥不建议直接用lock的值判断
        if (Boolean.TRUE.equals(lock)) {
            log.info("获取分布式锁成功...");

            // 加锁成功, 执行业务
            // 设置过期时间, 必须和加锁是同步的, 原子的
//            stringRedisTemplate.expire("lock", 30, TimeUnit.SECONDS);
            Map<String, List<Catelog2VO>> catalogData;
            try {
                catalogData = getCatalogDataFromDBAndPutCache();
            } finally {
                // 删除锁 ---> 获取值对比 + 对比成功删除 = 原子操作 lua脚本解锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("lock"), uuid);

//            String lockValue = stringRedisTemplate.opsForValue().get("lock");
//            if (uuid.equals(lockValue)) {
//                // 删除我自己的锁
//                stringRedisTemplate.delete("lock");
//            }
            }

            return catalogData;
        } else {
            log.info("获取分布式锁失败, 等待重试...");
            // 加锁失败, 重试 自旋？
            //休眠一百毫秒
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogDataWithRedisLock();
        }
    }


    /**
     * Redis锁
     */
    @Deprecated
    public Map<String, List<Catelog2VO>> getCatalogDataWithLocalLock() {
        synchronized (this) {
            // ---> 得到锁以后, 我们应该去缓存中确定一次, 如果没有才需要继续查询
            String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
            if (StringUtils.isNotEmpty(catalogJson)) {
                return JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2VO>>>() {
                });
            }

            Map<String, List<Catelog2VO>> dataFromDB = getCatalogDataFromDB();
            // 锁, 时序问题
            stringRedisTemplate.opsForValue().set("catalogJson", JSON.toJSONString(dataFromDB));
            return dataFromDB;
        }
    }

    // ------------------------------------------------------------------------------------------------------


    /**
     * 从指定分类数据中, 找到指定父id下的所有子分类(相比较而言, 优化为在内存中过滤, 且减少与db的交互)
     *
     * @param categoryEntityList 给定的分类数据
     * @param parentCid          给定的父id
     * @return categoryEntityList#filter
     */
    private List<CategoryEntity> getSonCategories(List<CategoryEntity> categoryEntityList, Long parentCid) {
        return categoryEntityList.stream().filter(categoryEntity -> categoryEntity.getParentCid().equals(parentCid)).collect(Collectors.toList());
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


    /**
     * 抽取的从数据库中获取封装的数据的方法
     *
     * @return map
     */
    public Map<String, List<Catelog2VO>> getCatalogDataFromDB() {
//        Map<String, List<Catelog2VO>> cacheData = (Map<String, List<Catelog2VO>>) cache.get("catalogJson");
//        if(cache.get("catalogJson") != null) {
//            log.info("****************getData from cache");
//            return cacheData;
//        }

        // 优化项:将数据库的多次查询变为一次
        List<CategoryEntity> categoryEntityList = this.baseMapper.selectList(null);

        // 1、获取所有一级分类(有优化)
        List<CategoryEntity> level1Categories = getSonCategories(categoryEntityList, 0L);

        // 2、封装数据
        return level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 每个一级分类下的二级分类
            List<CategoryEntity> level2Categories = getSonCategories(categoryEntityList, v.getCatId());

            // 根据二级分类进行结果封装
            List<Catelog2VO> catelog2VOList = new ArrayList<>();
            if (null != level2Categories) {
                catelog2VOList = level2Categories.stream().map(level2Category -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, level2Category.getCatId().toString(), level2Category.getName());

                    // 设置二级分类下的三级分类
                    List<CategoryEntity> level3Categories = getSonCategories(categoryEntityList, level2Category.getCatId());
                    if (null != level3Categories) {
                        List<Catelog2VO.Catelog3VO> catelog3VOList = level3Categories.stream()
                                .map(level3Category -> new Catelog2VO.Catelog3VO(level2Category.getCatId().toString(), level3Category.getCatId().toString(), level3Category.getName()))
                                .collect(Collectors.toList());
                        catelog2VO.setCatalog3List(catelog3VOList);
                    }

                    return catelog2VO;
                }).collect(Collectors.toList());
            }

            return catelog2VOList;
        }));

        // 给缓存中放一份
//        cache.put("catalogJson", map);
//        return map;
    }

    /**
     * put cache
     */
    public Map<String, List<Catelog2VO>> getCatalogDataFromDBAndPutCache() {
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isNotEmpty(catalogJson)) {
            return JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2VO>>>() {
            });
        } else {
            Map<String, List<Catelog2VO>> catalogDataFromDB = getCatalogDataFromDB();
            stringRedisTemplate.opsForValue().set("catalogJson", JSON.toJSONString(catalogDataFromDB));
            return catalogDataFromDB;
        }
    }
}
