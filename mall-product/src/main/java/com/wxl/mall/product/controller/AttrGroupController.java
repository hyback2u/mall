package com.wxl.mall.product.controller;

import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.R;
import com.wxl.mall.product.entity.AttrGroupEntity;
import com.wxl.mall.product.service.AttrGroupService;
import com.wxl.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 属性分组
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Slf4j
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private CategoryService categoryService;


    /**
     * 根据三级分类id列举分类下的属性
     *
     * @param params     params
     * @param categoryId 三级分类id
     * @return message&data
     */
    @RequestMapping("/list/{categoryId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("categoryId") Long categoryId) {
        log.info("**************list(), categoryId = {}", categoryId);
        PageUtils page = attrGroupService.queryPage(params, categoryId);

        return R.ok().put("page", page);
    }

//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params) {
//        PageUtils page = attrGroupService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }


    /**
     * 返回属性分组单个实体的信息
     * 这里返回的属性分组所在的三级分类的id, 但是并不是路径, 这里需要修改
     *
     * @param attrGroupId AttrGroupEntity的id
     * @return AttrGroupEntity实体
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        // categoryService具有的功能
        Long[] catelogPath = categoryService.findCatelogPathByCatelogId(attrGroup.getCatelogId());

        // 把所属分类的完整路径塞回去, 这时, 就想要一个service来提供这个功能了
        attrGroup.setCatelogPath(catelogPath);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
