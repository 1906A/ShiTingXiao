package com.leyou.client;

import com.leyou.pojo.Category;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryClientService {

    @RequestMapping("findById")
    public Category findById(@RequestParam("id") Long id);

    /**
     * 查询所有信息
     *
     * @return
     */
    @RequestMapping("findAll")
    public List<Category> findAll();
}
