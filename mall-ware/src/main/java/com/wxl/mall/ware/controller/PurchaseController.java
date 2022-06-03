package com.wxl.mall.ware.controller;

import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.R;
import com.wxl.mall.ware.entity.PurchaseEntity;
import com.wxl.mall.ware.service.PurchaseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 采购信息
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:30:26
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Resource
    private PurchaseService purchaseService;


    /**
     * 查询未领取的采购单
     * feature:采购需求合并到整单下拉选择数据源触发
     *
     * @param params params
     * @return pageData
     */
    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);

        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
