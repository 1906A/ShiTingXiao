package com.leyou.service;

import com.leyou.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsService {

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
    @Autowired
    TemplateEngine templateEngine;


    public Map<String, Object> item(Long spuId) {


        // 1:spu   √  可以得到cid3
        Spu spu = spuClient.findSpuBySpuId(spuId);


        //2:spudetail  商品详情   √
        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spuId);


        //3:sku
        List<Sku> skuList = skuClient.findSkuBySpuId(spuId);


        //4:规格参数组
        List<SpecGroup> specGroupList = specGroupClient.findSpecGroupByCid(spu.getCid3());


        //5:规格参数详情  cid+generic=0 list
        List<SpecParam> specParamList = specClient.findSpecParamByCidAndGeneric(spu.getCid3(), 0);
        // 得到 非通用的属性名称  规格参数的特殊属性
        Map<Long, String> paramMap = new HashMap<>();
        specParamList.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });


        //6:三级分类 category id name
        List<Category> categoryList = new ArrayList<>();

        Category cid1Nmae = categoryClient.findById(spu.getCid1());
        Category cid2Nmae = categoryClient.findById(spu.getCid2());
        Category cid3Nmae = categoryClient.findById(spu.getCid3());
        categoryList.add(cid1Nmae);
        categoryList.add(cid2Nmae);
        categoryList.add(cid3Nmae);


        //7:品牌  品牌 和标题
        Brand brand = brandClient.findById(spu.getBrandId());

        Map<String, Object> map = new HashMap<>();

        map.put("spu", spu);
        map.put("spuDetail", spuDetail);
        map.put("skuList", skuList);
        map.put("specGroupList", specGroupList);
        map.put("paramMap", paramMap);
        map.put("categoryList", categoryList);
        map.put("brand", brand);
        return map;
    }


    /**
     * 通过thyemleaf实现页面的静态化
     */
    public void creatHtml(Long spuId) {

        PrintWriter writer = null;
        try {
            // 1:创建上下文
            Context context = new Context();
            //2:把数据放入到上下文中
            context.setVariables(this.item(spuId));

            //3:写入文件,写入流
            File file = new File("D:\\Program Files\\nginx-1.16.1\\html\\" + spuId + ".html");
            writer = new PrintWriter(file);

            //4:执行静态化
            templateEngine.process("item", context, writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                //5:关闭写入流
                writer.close();
            }
        }

    }


    /**
     * 删除静态页面
     * @param spuId
     */
    public void deleteThymeleafData(Long spuId) {

        File file = new File("D:\\Program Files\\nginx-1.16.1\\html\\" + spuId + ".html");
        if (file != null && file.exists()){
            file.delete();
        }
    }
}
