package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 品牌管理中的列表分页查询
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @RequestMapping("page")
    public Object findBrandByPage(@RequestParam("key") String key,
                                  @RequestParam("page") Integer page,
                                  @RequestParam("rows") Integer rows,
                                  @RequestParam(value = "sortBy", defaultValue = "letter") String sortBy,
                                  @RequestParam(value = "desc", defaultValue = "false") boolean desc) {
        System.out.println("接受的参数:" + key + "==" + page + "==" + rows + "==" + sortBy + "===" + desc);
        PageResult<Brand> brandList = brandService.findBrand(key, page, rows, sortBy, desc);
        return brandList;
    }

    /**
     * 添加品牌
     *
     * @param brand
     * @param cids
     */
    @RequestMapping("addOrEditBrand")
    public void addOrEditBrand(Brand brand, @RequestParam("cids") List<String> cids) {
        //根据brand是否含有主键id来判断添加或者修改
        if (brand.getId() == null) {
            //添加
            //添加到数据库
            brandService.addOrEditBrand(brand);
            //添加关系表
            brandService.addBrandAndCategory(brand.getId(), cids);
        } else {
            //修改
            brandService.updateBrand(brand, cids);
        }

        // brandService.addOrEditBrand();
    }

    /**
     * 根据id 删除brand
     * restful 风格的接受参数
     *
     * @param id
     */
    @RequestMapping("deleteById/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        brandService.deleteById(id);
    }

    /**
     * @param id 根据品牌信息查询商品分类
     */
    @RequestMapping("bid/{id}")
    public List<Category> findCategoryById(@PathVariable("id") Long id) {
        return brandService.findCategoryById(id);
    }


    /**
     * 根据商品分类cid查询对应品牌
     *
     * @param cid
     * @return
     */
    @RequestMapping("cid/{cid}")
    public List<Brand> findBrandByCid(@PathVariable("cid") Long cid) {
        return brandService.findBrandByCid(cid);
    }

    /**
     * 根据品牌id查询品牌信息
     *
     * @param id
     * @return
     */
    @RequestMapping("findById")
    public Brand findById(@RequestParam("id") Long id) {
        return brandService.findById(id);
    }
}


