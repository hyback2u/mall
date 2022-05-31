package com.wxl.mall.product.controller;

import com.alibaba.fastjson.JSON;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.R;
import com.wxl.mall.product.service.AttrService;
import com.wxl.mall.product.vo.AttrResponseVO;
import com.wxl.mall.product.vo.AttrVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 商品属性
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 11:20:00
 */
@Slf4j
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Resource
    private AttrService attrService;


    /**
     * 获取分类下的规格参数功能[分页查询] xxxPage
     * eg:http://localhost:88/api/product/attr/base/list/0?t=1653922340504&page=1&limit=10&key=
     * <p>
     * 获取分类销售属性:/product/attr/sale/list/{catelogId}, 唯一变的是: base/sale
     * 这里抽取为一个方法:
     * 1、原始: @GetMapping("/base/list/{catelogId}")
     * 2、修改: @GetMapping("/{attrType}}/list/{catelogId}")
     *
     * @param attrType  0-销售属性，1-基本属性
     * @param params    封装的查询参数
     * @param catelogId 分类id
     * @return message
     */
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable("attrType") String attrType,
                      @PathVariable("catelogId") Long catelogId) {
        log.info("*************attrType = {}, catelogId = {}", attrType, catelogId);
        PageUtils page = attrService.queryBaseAttrPage(params, attrType, catelogId);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId) {
        // 这里返回的信息比较简单, 注释掉使用getAttrInfo()
//        AttrEntity attr = attrService.getById(attrId);
        AttrResponseVO attrRespVO = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attrRespVO);
    }

//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    public R save(@RequestBody AttrEntity attr) {
//        attrService.save(attr);
//
//        return R.ok();
//    }

    /**
     * 保存, 这里入参修改为VO, 为了能够多收集一些特定的需要的信息
     *
     * @param attr AttrVO, 可以收集来源于页面的这些数据
     * @return message
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVO attr) {
        attrService.saveAttrVO(attr);

        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    public R update(@RequestBody AttrEntity attr) {
//        attrService.updateById(attr);
//
//        return R.ok();
//    }


    /**
     * update修改
     *
     * @param attrResponseVO vo, 封装更新信息的vo
     * @return message
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrResponseVO attrResponseVO) {
//        attrService.updateById(attr);
        log.info("**********AttrController:update, params: {}", JSON.toJSONString(attrResponseVO));
        attrService.updateAttr(attrResponseVO);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
