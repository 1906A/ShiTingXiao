package com.leyou.search.controller;

import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.item.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    CategoryClient categoryClient;
    @Autowired
    BrandClient brandClient;

    @RequestMapping("/page")
    public SearchResult page(@RequestBody SearchRequest searchRequest) {

        System.out.println(searchRequest.getKey() + "=====" + searchRequest.getPage());
        //去es索引库做查询
        //查询条件构造器
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        //构造条件 从es all 中搜索
        builder.withQuery(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));

        //分页查询
        builder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getDefaultSize()));

        //根据新品排序
        builder.withSort(SortBuilders.fieldSort(searchRequest.getSortBy())
                .order(searchRequest.getDescending()? SortOrder.DESC:SortOrder.ASC));

        //加载分类和品牌
        //分类
        String categoryName="categoryName"; //聚合名称定为变量
        String brandName="brandName";
        //构造器
        builder.addAggregation(AggregationBuilders.terms(categoryName).field("cid3"));
        builder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));

        AggregatedPage<Goods> search = (AggregatedPage<Goods>) goodsRepository.search(builder.build());

        //构造分类信息-根据分类id获取名称
        LongTerms categoryAgg = (LongTerms) search.getAggregation(categoryName);
        List<Category> categoryList = new ArrayList<>();
        categoryAgg.getBuckets().forEach(bucket -> {
            Long categoryId =(Long)bucket.getKey();

            //根据分类id去数据库查询
            Category category = categoryClient.findById(categoryId);
            categoryList.add(category);
        });

        //构造品牌信息-根据品牌id获取名称
        LongTerms brandAgg = (LongTerms) search.getAggregation(brandName);
        List<Brand> brandList=new ArrayList<>();
        brandAgg.getBuckets().forEach(bucket -> {
            Long brandId= (Long) bucket.getKey();
            //根据品牌Id去数据库查询
            Brand brand=brandClient.findById(brandId);
            brandList.add(brand);
        });

        //执行查询 totalelement 总条数 totalpage 总页数 content 数据内容
       // Page<Goods> search = goodsRepository.search(builder.build());
        return new SearchResult(search.getTotalElements(), search.getContent(), search.getTotalPages(),categoryList,brandList);
    }

}
