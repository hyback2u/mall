package com.wxl.mall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author wangxl
 * @since 2022/5/28 9:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallThirdPartyApplicationTests {
    @Autowired
    OSSClient ossClient;

    @Test
    public void contextLoads() throws Exception {
        InputStream inputStream = new FileInputStream("E:\\worksapce\\ha.jpg");
        ossClient.putObject("devops-mall", "third.jpg", inputStream);

        // 关闭OSSClient
        ossClient.shutdown();

        System.out.println("****************上传完成");
    }
}
