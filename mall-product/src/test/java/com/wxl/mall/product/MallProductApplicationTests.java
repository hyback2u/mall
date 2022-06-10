package com.wxl.mall.product;

//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.OSSClientBuilder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wxl.mall.product.entity.BrandEntity;
import com.wxl.mall.product.service.BrandService;
import com.wxl.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author wangxl
 * @since 2022/4/30 14:48
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallProductApplicationTests {

    @Autowired
    BrandService brandService;

//    @Autowired
//    OSSClient ossClient;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /**
     * 测试Redis的简单使用
     */
    @Test
    public void stringRedisTemplateTest() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        // 保存
        ops.set("hello", "world_" + UUID.randomUUID());

        // 查询
        String hello = ops.get("hello");
        System.out.println("之前保存的数据是：" + hello);
    }


    /**
     * 测试:根据三级分类id查询出该分类的完整路径
     * ************完整路径: [2, 35, 227]
     */
    @Test
    public void findPathTest() {
        Long[] path = categoryService.findCatelogPathByCatelogId(227L);
        log.info("************完整路径: {}", Arrays.toString(path));
    }


    /**
     * start启动器方式测试oss使用
     */
    @Test
    public void ossStarterTest() throws Exception {
//        InputStream inputStream = new FileInputStream("E:\\worksapce\\ha.jpg");
//        ossClient.putObject("devops-mall", "wuhu.jpg", inputStream);
//
//        // 关闭OSSClient
//        ossClient.shutdown();
//
//        System.out.println("****************上传完成");
    }

    /**
     * 阿里云OSS云存储简单测试
     */
    @Test
    public void ossTest() throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "******************";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "******************";
        String accessKeySecret = "******************";

        String bucketName = "devops-mall";

//        // 创建OSSClient实例。
//        OSS ossClient1 = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        // 上传文件流
//        InputStream inputStream = new FileInputStream("E:\\worksapce\\wbb.jpg");
//        ossClient1.putObject(bucketName, "wbb.jpg", inputStream);
//
//        // 关闭OSSClient
//        ossClient1.shutdown();
//
//        System.out.println("****************上传完成");
    }


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
