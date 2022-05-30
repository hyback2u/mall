package com.wxl.mall.product.controller;

import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.R;
import com.wxl.mall.product.entity.CategoryEntity;
import com.wxl.mall.product.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品三级分类
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    //    @Autowired
    @Resource // 没啥, 就是这样的话编译器不会有warring~
    private CategoryService categoryService;

    /**
     * 查出所有分类以及子分类, 以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    public R listAsTree() {
        List<CategoryEntity> categoryEntityList = categoryService.listWithTree();

        return R.ok().put("data", categoryEntityList);
    }

    /**
     * 列表
     * 逆向生成的方法, 查询所有的分类, 不过是分页查询
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     * 在更新分类实例的同时, 更新关联的所有的冗余表
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        // 级联更新所有的关联数据
        categoryService.updateCascade(category);

        return R.ok();
    }


    /**
     * 拖拽功能, 将商品分类拖拽后收集到的需要进行修改的数据进行批量更新
     *
     * @param categoryEntities 需要修改的category数组
     * @return message
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] categoryEntities) {
        categoryService.updateBatchById(Arrays.asList(categoryEntities));

        return R.ok();
    }


    /**
     * 删除
     * 这里是逆向生成的删除功能
     * 注释: @RequestBody是要获取请求体里的数据 ==> post请求才有请求体, 所以必须发送post
     * SpringMVC会自动将请求体的数据(json)转为对应的对象, 这里可以使用postman进行模拟调用测试
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        // 1、检查当前删除的菜单, 是否被别的地方引用了

        // note: 这个逻辑, 是自动生成的, 这里把它注释掉, 我们并不能直接删除
//		categoryService.removeByIds(Arrays.asList(catIds));

        categoryService.removeConditionalByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
