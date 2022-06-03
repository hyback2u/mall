package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.ProductAttrValueDao;
import com.wxl.mall.product.entity.ProductAttrValueEntity;
import com.wxl.mall.product.service.ProductAttrValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }


    /**
     * 根据spuId进行回显属性, 获取spu规格参数
     *
     * @param spuId spuId
     * @return data
     */
    @Override
    public List<ProductAttrValueEntity> baseAttrListForSpu(Long spuId) {
        return this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>()
                .eq("spu_id", spuId));
    }

    /**
     * 更新spu的基本属性
     *
     * @param spuId    spuId
     * @param entities 基本属性List
     */
    @Transactional
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities) {
        // 1.删除spuId之前对应的所有属性
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>()
                .eq("spu_id", spuId));

        // 2.更新(写入)操作
        for (ProductAttrValueEntity entity : entities) {
            entity.setSpuId(spuId);
        }
        // 批量保存
        this.saveBatch(entities);
    }

}
