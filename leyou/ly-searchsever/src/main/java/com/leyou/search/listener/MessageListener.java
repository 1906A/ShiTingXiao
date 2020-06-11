package com.leyou.search.listener;

import com.leyou.search.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @Autowired
    GoodsService goodsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "item.edit.search.queue",durable = "true"),
            exchange = @Exchange(
                    name = "item.exchanges",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void editEsData(Long spuId) throws Exception {
        System.out.println("开始监听修改ES的数据:"+spuId);

        goodsService.editEsData(spuId);

        System.out.println("修改数据监听结束");

    }
}
