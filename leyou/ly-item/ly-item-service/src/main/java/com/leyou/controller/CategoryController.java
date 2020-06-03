package com.leyou.controller;

import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /**
     * 根据节点 pid 查询所有
     *
     * @param pid
     * @return
     */
    @RequestMapping("list")
    public List<Category> list(@RequestParam("pid") Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryService.findCategory(category);
    }

    /**
     * 添加分类信息
     *
     * @param category
     * @return
     */
    @RequestMapping("add")
    public String add(@RequestBody Category category) {
        String result = "SUCC";
        try {
            categoryService.addCategory(category);
        } catch (Exception e) {
            System.out.println("添加商品分类异常");
            result = "FAIL";
        }
        return result;
    }

    /**
     * 修改分类信息
     *
     * @param category
     * @return
     */
    @RequestMapping("update")
    public String update(@RequestBody Category category) {
        String result = "SUCC";
        try {
            categoryService.updateCategory(category);
        } catch (Exception e) {
            System.out.println("修改商品分类异常");
            result = "FAIL";
        }
        return result;
    }

    /**
     * 根据节点ID删除商品信息
     *
     * @param id
     * @return
     */
    @RequestMapping("delete")
    public String delete(@RequestParam("id") Long id) {
        String result = "SUCC";
        try {
            categoryService.delectCategoryById(id);
        } catch (Exception e) {
            System.out.println("删除商品信息失败");
            result = "FAIL";
        }
        return result;
    }

    /**
     * 根据id查询商品信息
     *
     * @param id
     * @return
     */
    @RequestMapping("findById")
    public Category findById(@RequestParam("id") Long id) {
        return categoryService.findById(id);
    }
}
