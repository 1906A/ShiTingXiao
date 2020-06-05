package com.leyou.client;

import com.leyou.pojo.SpecParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("specParam")
public interface SpecClientService {


    /**
     * 根据cid(商品分类id)查询规格参数
     * @param cid
     * @return
     */
    @RequestMapping("params")
    public List<SpecParam> findSpecParamByCid(@RequestParam("cid") Long cid);

    @RequestMapping("paramsByCidAndSearching")
    public List<SpecParam> findSpecParamByCidAndSearching(@RequestParam("cid") Long cid);

    @RequestMapping("findSpecParamByCidAndGeneric")
    public List<SpecParam> findSpecParamByCidAndGeneric(@RequestParam("cid") Long cid,@RequestParam("generic") Integer generic);




}
