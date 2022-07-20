package com.wxl.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxl.common.utils.PageUtils;
import com.wxl.common.utils.Query;
import com.wxl.mall.order.dao.OrderItemDao;
import com.wxl.mall.order.entity.OrderItemEntity;
import com.wxl.mall.order.entity.OrderReturnReasonEntity;
import com.wxl.mall.order.service.OrderItemService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;


@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }


    /**
     * queues:声明需要监听的所有队列
     * org.springframework.amqp.core.Message
     *
     * 参数可以写以下类型:
     * 1、Message:原生消息详细信息, 头 + 体
     * 2、T<发送的消息的类型> OrderReturnReasonEntity
     * 3、Channel channel:当前传输数据的通道
     *
     * Queue:可以很多人监听, 只要收到消息, 队列删除消息, 而且只能有一个人收到此消息
     */
    @RabbitListener(queues = {"hello-java-queue"})
    public void receiveMessage(Message message, OrderReturnReasonEntity content) {
        // 消息体
        byte[] body = message.getBody();
        System.out.println("body: " + Arrays.toString(body));

        // 消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();
        System.out.println("properties: " + messageProperties);

        System.out.println("接收到的消息...内容:" + message + " ==> 类型: " + message.getClass());
        System.out.println("入参接收的消息体的真正内容: " + content);

//        System.out.println("channel = " + channel);
    }
}
