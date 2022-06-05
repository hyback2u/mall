package com.wxl.mall.ware.controller;

import com.wxl.common.to.SkuHasStockVO;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.R;
import com.wxl.mall.ware.entity.WareSkuEntity;
import com.wxl.mall.ware.service.WareSkuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 商品库存
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:30:26
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Resource
    private WareSkuService wareSkuService;


    /**
     * 批量查询sku是否有库存(请求体里面放数据->POST)
     *
     * @param skuIds skuIds
     * @return data&List_SkuHasStockVO
     */
    @PostMapping("/hasStock")
    public List<SkuHasStockVO> getSkusHasStock(@RequestBody List<Long> skuIds) {
        // sku_id, hasStock
        return wareSkuService.getSkusHasStock(skuIds);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
