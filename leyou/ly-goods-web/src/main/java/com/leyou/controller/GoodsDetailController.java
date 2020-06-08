package com.leyou.controller;

import com.leyou.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private BrandClient brandClient;

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
     * 7:品牌
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
        Map<Long,String> paramMap = new HashMap<>();
        specParamList.forEach(param ->{
            paramMap.put(param.getId(),param.getName());
        });
        model.addAttribute("paramMap",paramMap);

        //6:三级分类 category id name
        List<Category> categoryList =new ArrayList<>();
       // List<Category> categoryList = categoryClient.findAll()
        //有spu 可以得到 cid1  cid2  cid3
        Category cid1Nmae = categoryClient.findById(spu.getCid1());
        Category cid2Nmae = categoryClient.findById(spu.getCid2());
        Category cid3Nmae = categoryClient.findById(spu.getCid3());
        categoryList.add(cid1Nmae);
        categoryList.add(cid2Nmae);
        categoryList.add(cid3Nmae);
        model.addAttribute("categoryList",categoryList);

        //7:品牌  品牌 和标题
        Brand brand = brandClient.findById(spu.getBrandId());
        model.addAttribute("brand",brand);


        return "item";
    }
}
