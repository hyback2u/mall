package com.wxl.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxl.common.utils.PageUtils;
import com.wxl.mall.ware.vo.MergeVO;
import com.wxl.mall.ware.entity.PurchaseEntity;
import com.wxl.mall.ware.vo.PurchaseDoneVO;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author wangxl
 * @email 1919543837@qq.com
 * @date 2022-04-30 15:30:26
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未领取的采购单
     *
     * @param params params
     * @return pageData
     */
    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);


    /**
     * 合并采购需求
     *
     * @param mergeVO 整单id&合并项集合
     */
    void mergePurchase(MergeVO mergeVO);

    /**
     * 领取采购单
     *
     * @param ids 采购单ids
     */
    void received(List<Long> ids);


    /**
     * 完成采购单
     *
     * @param purchaseDoneVO 采购单ids
     */
    void finishPurchase(PurchaseDoneVO purchaseDoneVO);
}

