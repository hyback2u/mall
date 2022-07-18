package com.wxl.mall.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * AmqpAdmin的使用, 创建、删除队列交换机以及绑定关系
 *
 * @author wangxl
 * @since 2022/7/18 23:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MallOrderApplicationTests {

    @Autowired
    private AmqpAdmin amqpAdmin;

    /**
     * 1、如何创建Exchange, Queue, Binding  使用AmqpAdmin(管理...)进行创建
     * 2、如何收发消息
     */
    @Test
    public void createExchange() {
        // AllArgs: DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        DirectExchange directExchange = new DirectExchange("hello-java-exchange", true, false, null);
        amqpAdmin.declareExchange(directExchange);

        log.info("Exchange[{}]创建成功", "hello-java-exchange");
    }


    /**
     * 创建队列
     */
    @Test
    public void createQueue() {
        // AllArgs: Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        Queue queue = new Queue("hello-java-queue", true, false, false, null);
        amqpAdmin.declareQueue(queue);

        log.info("Queue[{}]创建成功", "hello-java-queue");
    }


    /**
     * 创建绑定
     */
    @Test
    public void createBinding() {
        // 1、destination:目的地 2、destinationType:目的地类型 3、exchange:交换机 4、routingKey:路由键 5、自定义参数
        // 总结:将 exchange 指定的交换机和 destination 目的地进行绑定, 使用 destinationType 指定类型, 使用 routingKey 作为路由键
        // AllArgs: Binding(String destination, DestinationType destinationType, String exchange, String routingKey, Map<String, Object> arguments)
        Binding binding = new Binding("hello-java-queue", Binding.DestinationType.QUEUE, "hello-java-exchange",
                "hello.java", null);
        amqpAdmin.declareBinding(binding);

        log.info("Binding[{}]创建成功", "hello-java-queue");
    }
}
