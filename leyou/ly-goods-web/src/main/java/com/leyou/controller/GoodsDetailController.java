package com.leyou.controller;

import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class GoodsDetailController {


    @Autowired
    GoodsService goodsService;

    @RequestMapping("hello")
    public String hello(Model model) {
        String name = "张三";
        model.addAttribute("name", name);
        return "hello";
    }

    /**
     * 请求商品详情的微服务测试
     * 1:spu
     * 2:spudetail
     * 3:sku
     * 4:规格参数组
     * 5:规格参数详情
     * 6:三级分类
     * 7:品牌
     *
     * @param model
     * @param spuId
     * @return
     */
    @RequestMapping("item/{spuId}.html")
    public String item(Model model, @PathVariable("spuId") Long spuId) {
        //查询
        Map<String, Object> map = goodsService.item(spuId);
        model.addAllAttributes(map);
        //静态化
        goodsService.creatHtml(spuId);

        return "item";
    }


}
