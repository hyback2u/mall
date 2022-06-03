package com.wxl.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.product.dao.SpuImagesDao;
import com.wxl.mall.product.entity.SpuImagesEntity;
import com.wxl.mall.product.service.SpuImagesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(new Query<SpuImagesEntity>().getPage(params), new QueryWrapper<>());

        return new PageUtils(page);
    }

    @Override
    public void saveImages(Long spuId, List<String> images) {
        if (null == images || images.size() == 0) {
            return;
        }
        List<SpuImagesEntity> imagesEntityList = images.stream().map(img -> {
            SpuImagesEntity imageEntity = new SpuImagesEntity();
            imageEntity.setSpuId(spuId);
            imageEntity.setImgUrl(img);
            return imageEntity;
        }).collect(Collectors.toList());

        this.saveBatch(imagesEntityList);
    }

}
