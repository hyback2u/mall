package com.wxl.mall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.R;
import com.wxl.mall.product.entity.BrandEntity;
import com.wxl.mall.product.entity.CategoryBrandRelationEntity;
import com.wxl.mall.product.service.CategoryBrandRelationService;
import com.wxl.mall.product.vo.BrandVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 品牌分类关联
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    //    @Autowired
    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;


    /**
     * 根据三级分类id查询关联的所有品牌
     * note:参数, 不是路径变量, 应该用@RequestParam注解, 且是必须存在的, 可以add校验 required(true)
     *
     * 总结:
     * 1、Controller:只是来接收请求和处理页面提交来的数据, 把数据封装成业务想要的
     * 或者对数据做一些前置校验(controller:处理请求, 接收和校验数据)
     * 2、Service:接收controller传来的数据, 进行业务处理 --> 尽可能的做到通用与抽象
     * 3、最后Controller接收Service处理完的数据, 封装成页面指定的VO
     *
     *
     * @param catId 三级分类id
     * @return data(brandId, brandName) -> 抽取单独VO
     */
    @GetMapping(value = "/brands/list")
    public R relationBrandsList(@RequestParam(value = "catId") Long catId) {
        // 1、关于这里返回List还是分页, 看前端要什么
        // 2、service不推荐直接返回VO, 如果service擦查询出的是完整的品牌, 别人可能还要用
//        List<BrandVO> brandVOList = categoryBrandRelationService.getBrandsByCatelogId(catId);
        List<BrandEntity> brandEntities = categoryBrandRelationService.getBrandsByCatelogId(catId);

        // 在controller里面封装想要用的vo
        List<BrandVO> brandVOList = brandEntities.stream().map(item -> {
            BrandVO brandVO = new BrandVO();

            brandVO.setBrandId(item.getBrandId());
            brandVO.setBrandName(item.getName());

            return brandVO;
        }).collect(Collectors.toList());

        return R.ok().put("data", brandVOList);
    }


    /**
     * 获取当前品牌关联的所有分类
     *
     * @param brandId 品牌id
     * @return 分类id+分类name
     */
//    @RequestMapping(value = "/catelog/list", method = RequestMethod.GET)
    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam("brandId") Long brandId) {
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId)
        );

        return R.ok().put("data", data);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 新增品牌与分类的关联关系
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
