package com.wxl.mall.product.controller;

import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.R;
import com.wxl.mall.product.entity.SpuInfoEntity;
import com.wxl.mall.product.service.SpuInfoService;
import com.wxl.mall.product.vo.SPUSaveVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * spu信息
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Resource
    private SpuInfoService spuInfoService;

    /**
     * 商品上架
     *
     * @param spuId spuId
     * @return message
     */
    @PostMapping(value = "/{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId) {
        spuInfoService.up(spuId);

        return R.ok();
    }


    /**
     * spu检索
     *
     * @param params params
     * @return pageData
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }


    /**
     * 保存页面发布商品SPU信息
     * 这个动作报错牵扯很多动作, 涉及很多张表, 不仅保存商品表里边的sku, spu
     * 包括图片信息, 属性值关联... 以及设计了商品的积分, 还要跨系统给商品的营销系统里面保存sku积分满减等
     */
    @RequestMapping("/save")
    public R save(@RequestBody SPUSaveVO spuSaveVO) {
//        spuInfoService.save(spuSaveVO);
        spuInfoService.saveSPUInfo(spuSaveVO);

        return R.ok();
    }


//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    public R save(@RequestBody SpuInfoEntity spuInfo){
//		spuInfoService.save(spuInfo);
//
//        return R.ok();
//    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SpuInfoEntity spuInfo) {
        spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
