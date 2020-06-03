package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.PageResult;
import com.leyou.dao.BrandMapper;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;


    /**
     * 1. 使用pageHelper
     * 2. 手写sql分页 limit关键字
     * 3. 通用Mapper Excempe 条件构造器 用java代码封装sql语句
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Brand> findBrand(String key, Integer page, Integer rows, String sortBy, boolean desc) {
        // PageHelper
        PageHelper.startPage(page, rows);

        List<Brand> brandList = brandMapper.findBrand(key, sortBy, desc);

        PageInfo<Brand> brandPageInfo = new PageInfo<>(brandList);

        return new PageResult<Brand>(brandPageInfo.getTotal(), brandPageInfo.getList());
    }

    /**
     * 手写limit sql
     * 分页公式 (page-1)*rows , rows
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Brand> findBrandByLimit(String key, Integer page, Integer rows, String sortBy, boolean desc) {
        // PageHelper

        //查询指定分页信息
        List<Brand> brandList = brandMapper.findBrandLimit(key, (page - 1) * rows, rows, sortBy, desc);

        //total总页数
        Long total = brandMapper.findBrandCount(key, sortBy, desc);


        return new PageResult<Brand>(total, brandList);
    }

    public void addOrEditBrand(Brand brand) {
        brandMapper.insert(brand);
    }


    public void addBrandAndCategory(Long id, List<String> cids) {
        cids.forEach(cid -> {
            brandMapper.addBrandAndCategory(id, Long.parseLong(cid));
        });
    }

    public void deleteById(Long id) {
        // 1: 先删除关系表
        brandMapper.deleteBrandAndCategoryById(id);
        // 2: 然后删除当前表
        brandMapper.deleteByPrimaryKey(id);
    }


    public List<Category> findCategoryById(Long id) {
        return brandMapper.findCategoryById(id);
    }

    public void updateBrand(Brand brand, List<String> cids) {
        /**
         *  1: 修改品牌表
         *  2: 修改品牌和商品关联表 :做法,先删除,在添加
         * */
        // 1: 修改品牌表
        brandMapper.updateByPrimaryKey(brand);
        // 2: 修改品牌和商品关联表
        brandMapper.deleteBrandAndCategoryById(brand.getId());
        cids.forEach(cid -> {
            brandMapper.addBrandAndCategory(brand.getId(), Long.parseLong(cid));
        });

    }

    public List<Brand> findBrandByCid(Long cid) {
        return brandMapper.findBrandByCid(cid);
    }

    /**
     * 根据品牌id查询品牌信息
     *
     * @param id
     * @return
     */
    public Brand findById(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }
}
