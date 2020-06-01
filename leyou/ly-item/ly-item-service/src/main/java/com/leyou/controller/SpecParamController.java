package com.leyou.controller;

import com.leyou.pojo.SpecParam;
import com.leyou.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("specParam")
public class SpecParamController {

    @Autowired
    private SpecParamService specParamService;



    /**
     * 保存or修改规格参数
     * @param specParam
     */
    @RequestMapping("param")
    public void saveSpecParam(@RequestBody SpecParam specParam){
        //根据id判断修改或删除
        if (specParam.getId() != null){
            specParamService.updateSpecParam(specParam);
        }else {
            specParamService.saveSpecParam(specParam);
        }
    }

    /**
     * 根据id删除商品规格参数
     * @param id
     */
    @RequestMapping("param/{id}")
    public void delectSpecParam(@PathVariable("id") Long id){
       specParamService.delectSpecParam(id);
    }


    /**
     * 根据cid(商品分类id)查询规格参数
     * @param cid
     * @return
     */
    @RequestMapping("params")
    public List<SpecParam> findSpecParamByCid(@RequestParam("cid") Long cid){
        return specParamService.findSpecParamByCid(cid);
    }


    /**
     * 根据cid(商品分类id)和可搜索参数searching:默认ture查询规格参数
     * @param cid
     * @return
     */
    @RequestMapping("paramsByCidAndSearching")
    public List<SpecParam> findSpecParamByCidAndSearching(@RequestParam("cid") Long cid){
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setSearching(true);
        return specParamService.findSpecParamByCidAndSearching(specParam);
    }

}
