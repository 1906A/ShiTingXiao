package com.leyou.client;

import com.leyou.pojo.SpecGroup;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("spec")
public interface SpecGroupClientService {

    /**
     * 根据cid查询商品规格组列表
     * @param cid
     * @return
     */
    @RequestMapping("groups/{cid}")
    public List<SpecGroup> findSpecGroupByCid(@PathVariable("cid") Long cid);
}
