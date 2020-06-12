package com.leyou.listener;

import com.leyou.service.GoodsService;
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
            value = @Queue(name = "item.edit.web.queue",durable = "true"),
            exchange = @Exchange(
                    name = "item.exchanges",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void editThymeleafData(Long spuId) throws Exception {
        System.out.println("开始监听修改Thymeleaf的数据:"+spuId);

        if (spuId == null){
            return;
        }else {
            goodsService.creatHtml(spuId);
        }

        System.out.println("修改数据Thymeleaf监听结束");

    }


    /**
     * 监听删除信息
     * @param spuId
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "item.delete.web.queue",durable = "true"),
            exchange = @Exchange(
                    name = "item.exchanges",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void deleteThymeleafData(Long spuId) throws Exception {
        System.out.println("开始监听删除Thymeleaf的数据:"+spuId);

        if (spuId == null){
            return;
        }else {
            goodsService.deleteThymeleafData(spuId);
        }

        System.out.println("sc数据Thymeleaf监听结束");

    }
}
