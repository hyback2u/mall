package com.wxl.mall.member;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author wangxl
 * @since 2022/6/26 18:28
 */
public class MallMemberApplicationTests {


    /**
     * md5 & md5 盐值加密
     */
    @Test
    public void md5Test() {
        String md5Hex = DigestUtils.md5Hex("123456");
        // e10adc3949ba59abbe56e057f20f883e
        System.out.println(md5Hex);

        // 盐值加密:随机值
        String md5Crypt = Md5Crypt.md5Crypt("123456".getBytes(), "$1$qqq");
        System.out.println(md5Crypt);

        // 验证的时候, 拿着盐(数据库查), 然后对比 md5
    }

    /**
     * Spring 5编解码操作
     */
    @Test
    public void springCodeTest() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");

        boolean matches1 = passwordEncoder.matches("123456", "$2a$10$//CkrDy4rFiFCy7d6DivZOJLeDPeRWKnaby1HkwBgvcr3zXzduOkC");
        boolean matches2 = passwordEncoder.matches("123456", "$2a$10$OWZ78gvo3xD.nPZdjR5bz.4a0d6/lAhQo45yrfJt4Ru2aJgZZzlae");
        // $2a$10$//CkrDy4rFiFCy7d6DivZOJLeDPeRWKnaby1HkwBgvcr3zXzduOkC
        // $2a$10$OWZ78gvo3xD.nPZdjR5bz.4a0d6/lAhQo45yrfJt4Ru2aJgZZzlae
        System.out.println(encode);

        // 卧槽... 匹配了呀...
        System.out.println("matches1:" + matches1);
        System.out.println("matches2:" + matches2);
    }
}
