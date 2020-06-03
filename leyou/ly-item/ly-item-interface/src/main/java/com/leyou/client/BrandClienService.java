package com.leyou.client;

import com.leyou.pojo.Brand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("brand")
public interface BrandClienService {
    @RequestMapping("findById")
    public Brand findById(@RequestParam("id") Long id);
}
