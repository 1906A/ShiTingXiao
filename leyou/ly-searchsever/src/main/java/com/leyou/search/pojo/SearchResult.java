package com.leyou.search.pojo;

import com.leyou.common.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.search.item.Goods;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {

    private List<Category> categoryList;

    private List<Brand> brandList;

    private List<Map<String,Object>> paramList;

    public SearchResult(Long total, List<Goods> items, Integer totalPage, List<Category> categoryList,
                        List<Brand> brandList,List<Map<String, Object>> paramList) {
        super(total, items, totalPage);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.paramList = paramList;
    }



    public List<Map<String, Object>> getParamList() {
        return paramList;
    }

    public void setParamList(List<Map<String, Object>> paramList) {
        this.paramList = paramList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }


}
