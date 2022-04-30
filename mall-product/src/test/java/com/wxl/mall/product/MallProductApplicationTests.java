package com.wxl.mall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxl.mall.product.entity.BrandEntity;
import com.wxl.mall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author wangxl
 * @since 2022/4/30 14:48
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallProductApplicationTests {

    @Autowired
    BrandService brandService;


    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("华为");

        brandService.save(brandEntity);

        System.out.println("保存成功");
    }


    @Test
    public void testUpdateById() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("这是 HUAWEI");

        brandService.updateById(brandEntity);

        System.out.println("修改成功");
    }


    @Test
    public void testQuery() {
        List<BrandEntity> brandEntityList =
                brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));

        brandEntityList.forEach(System.out::println);
    }
}
