package com.leyou.service;

import com.leyou.dao.CategoryMapper;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 根据节点ID查询所有分类信息
     *
     * @param category
     * @return
     */
    public List<Category> findCategory(Category category) {
        return categoryMapper.select(category);
    }

    /**
     * 测试
     *
     * @param id
     * @return
     */
    public Category findCate(int id) {
        return categoryMapper.findCate(id);
    }

    /**
     * 添加分类信息
     *
     * @param category
     */
    public void addCategory(Category category) {
        categoryMapper.insertSelective(category);
    }

    /**
     * 修改分类信息
     *
     * @param category
     */
    public void updateCategory(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    /**
     * 根据节点ID删除商品信息
     *
     * @param id
     */
    public void delectCategoryById(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id查询商品信息
     *
     * @param id
     * @return
     */
    public Category findById(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }
}
