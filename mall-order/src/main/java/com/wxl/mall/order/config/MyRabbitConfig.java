package com.wxl.mall.order.config;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author wangxl
 * @since 2022/7/19 19:51
 */
@Configuration
public class MyRabbitConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用Json序列化机制, 进行消息转换
     *
     * @return MessageConverter
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * MyRabbitConfig对象创建完成之后, 执行这个方法
     * 1、配置:spring.publisher-confirms: true # 开启发送端确认
     * 2、设置确认回调
     */
    @PostConstruct
    public void initRabbitTemplate() {
        // 设置确认回调, 消息收到后, 这个方法就会自动回调
        rabbitTemplate.setConfirmCallback(
                new RabbitTemplate.ConfirmCallback() {
                    /**
                     * @param correlationData 当前消息的唯一关联数据(消息的唯一id)
                     * @param ack 消息是否成功收到
                     * @param cause 失败的原因
                     */
                    @Override
                    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                        System.out.println("confirm... correlationData = [" + correlationData + "], ack = [" + ack + "], ack = [" + ack + "]");
                    }
                }
        );
    }
}
