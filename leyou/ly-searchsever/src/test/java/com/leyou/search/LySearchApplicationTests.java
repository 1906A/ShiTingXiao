package com.leyou.search;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leyou.common.PageResult;
import com.leyou.search.client.SpuClient;
import com.leyou.search.item.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.GoodsService;
import com.leyou.vo.SpuVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LySearchApplicationTests {

    @Autowired
    SpuClient spuClient;
    @Autowired
    GoodsService goodsService;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    GoodsRepository goodsRepository;

    @Test
    public void contextLoads() {
        //创建前先删除一下原来的
        elasticsearchTemplate.deleteIndex(Goods.class);
        //创建索引库和映射
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
        PageResult<SpuVo> pageResult = spuClient.findSpuByPage("", 1, 200, 2);
        pageResult.getItems().forEach(spuVo -> {
            System.out.println(spuVo.getId());
            try {
                Goods goods = goodsService.convert(spuVo);
                //存入es
                goodsRepository.save(goods);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }


    @Test
    public void findGoods(){
       // elasticsearchTemplate.deleteIndex(Goods.class);
        elasticsearchTemplate.createIndex(Goods.class);
    }
}
