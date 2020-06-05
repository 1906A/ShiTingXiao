package com.leyou.controller;

import com.leyou.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class GoodsDetailController {


    @Autowired
    private SpuClient spuClient;
    @Autowired
    private SkuClient skuClient;
    @Autowired
    private SpecGroupClient specGroupClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private CategoryClient categoryClient;

    @RequestMapping("hello")
    public String hello(Model model){
        String name ="张三";
        model.addAttribute("name",name);
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
     *
     * @param model
     * @param spuId
     * @return
     */
    @RequestMapping("item/{spuId}.html")
    public String item(Model model,@PathVariable("spuId") Long spuId){

        System.out.println("1111");
        // 1:spu
        Spu spu = spuClient.findSpuBySpuId(spuId);
        model.addAttribute("spu",spu);

        //2:spudetail
        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spuId);
        model.addAttribute("spuDetail",spuDetail);

        //3:sku
        List<Sku> skuList =skuClient.findSkuBySpuId(spuId);
        model.addAttribute("skuList",skuList);

        //4:规格参数组
        List<SpecGroup> specGroupList = specGroupClient.findSpecGroupByCid(spu.getCid3());
        model.addAttribute("specGroupList",specGroupList);


        //5:规格参数详情  cid+generic=0 list
        List<SpecParam> specParamList = specClient.findSpecParamByCidAndGeneric(spu.getCid3(), 0);
        model.addAttribute("specParamList",specParamList);

        //6:三级分类 category id name
        List<Category> categoryList = categoryClient.findAll();
        model.addAttribute("categoryList",categoryList);
        return "item";
    }
}
