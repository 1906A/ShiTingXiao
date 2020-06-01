package com.leyou.controller;

import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import com.leyou.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecGroupService specGroupService;


    /**
     * 根据cid查询商品规格组列表
     * @param cid
     * @return
     */
    @RequestMapping("groups/{cid}")
    public List<SpecGroup> findSpecGroupByCid(@PathVariable("cid") Long cid){
        return specGroupService.findSpecGroupByCid(cid);
    }

    /**
     * 保存商品规格组
     * @param specGroup
     */
    @RequestMapping("group")
    public void saveSpecGroup(@RequestBody SpecGroup specGroup){

        //根据id判断修改或添加
        if(specGroup.getId() != null){
            specGroupService.updateSpecGroup(specGroup);
        }else {
            specGroupService.saveSpecGroup(specGroup);
        }
    }

    /**
     * 根据分类id删除商品规格组
     * @param id
     */
    @RequestMapping("group/{id}")
    public void delectBySpecGroupId(@PathVariable("id") Long id){
        specGroupService.delectBySpecGroupId(id);
    }

    /**
     * 根据组id查询组参数
     * @param *
     * @return
     */
    @RequestMapping("params")
    public List<SpecParam> findSpecParamByGid(@RequestParam("gid") Long gid){
        return specGroupService.findSpecParamByGid(gid);
    }


}
