package com.leyou.client;

import com.leyou.common.PageResult;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.vo.SpuVo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@RequestMapping("spu")
public interface SpuClientService {

    @RequestMapping("page")
    public PageResult<SpuVo> findSpuByPage(@RequestParam("key") String key,
                                           @RequestParam("page") Integer page,
                                           @RequestParam("rows") Integer rows,
                                           @RequestParam("saleable") Integer saleable);

    @RequestMapping("detail/{spuId}")
    public SpuDetail findSpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据spuId查询spu信息
     *
     * @param spuId
     * @return
     */
    @RequestMapping("findSpuBySpuId")
    public Spu findSpuBySpuId(@RequestParam("spuId") Long spuId);
}
