package com.wxl.mall.thirdparty.component;

import com.wxl.mall.thirdparty.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信服务组件
 *
 * @author wangxl
 * @since 2022/6/26 11:36
 */
@Component
public class SmsComponent {

    @Value("${spring.cloud.alicloud.sms.host}")
    private String host;

    @Value("${spring.cloud.alicloud.sms.path}")
    private String path;

    @Value("${spring.cloud.alicloud.sms.appcode}")
    private String appcode;

    @Value("${spring.cloud.alicloud.sms.sign-id}")
    private String smsSignId;

    @Value("${spring.cloud.alicloud.sms.template-id}")
    private String templateId;

    public void sendSmsCode(String mobilePhone, String verificationCode) {
        String method = "POST";

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", mobilePhone);
        querys.put("param", "**code**:" + verificationCode + ",**minute**:5");
        querys.put("smsSignId", smsSignId);
        querys.put("templateId", templateId);
        Map<String, String> bodys = new HashMap<>();


        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
