package com.leyou.client;

import com.leyou.pojo.SpecParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("specParam")
public interface SpecClientService {

    @RequestMapping("params")
    public List<SpecParam> findSpecParamByCid(@RequestParam("cid") Long cid);

    @RequestMapping("paramsByCidAndSearching")
    public List<SpecParam> findSpecParamByCidAndSearching(@RequestParam("cid") Long cid);

}
