package com.wxl.mall.order.controller;

import com.wxl.mall.order.entity.OrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author wangxl
 * @since 2022/7/20 21:01
 */
@Slf4j
@RestController
public class RabbitController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试发送消息
     */
    @GetMapping("/sendMq")
    public void sendMessage() {
//        String msg = "Hello World";
        OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
        entity.setId(1L);

        entity.setCreateTime(new Date());

        // 如果发送的消息是个对象, 我们会使用序列化机制, 将对象写出去... 因此要求对象必须实现Serializable接口

        for (int i = 0; i < 10; i++) {
            entity.setName("退换货：" + i);
            rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", entity);
            log.info("**************************消息发送完成: {}", entity);
        }
    }
}
