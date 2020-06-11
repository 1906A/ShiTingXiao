package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.SpuService;
import com.leyou.vo.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spu")
public class SpuController {
    @Autowired
    private SpuService spuService;



    /**
     * 根据分页信息查询商品参数列表
     * @param key
     * @param page
     * @param rows
     * @param saleable
     * @return
     */
    @RequestMapping("page")
    public PageResult<SpuVo> findSpuByPage(@RequestParam("key") String key,
                                         @RequestParam("page") Integer page,
                                         @RequestParam("rows") Integer rows,
                                         @RequestParam(required = false,value = "saleable") Integer saleable) {
        System.out.println(saleable);
        return spuService.findSpuByPage(key,page,rows,saleable);
    }

    /**
     * 新增商品
     * @param spuVo
     */
    @RequestMapping("goods")
    public void addGoods(@RequestBody SpuVo spuVo){
        if (spuVo.getId() !=null){
            //修改
            spuService.updateGoods(spuVo);
        }else {
            //新增
            spuService.addGoods(spuVo);
        }

    }


    /**
     * 根据spuid查询spudetail数据
     * @param spuId
     * @return
     */
    @RequestMapping("detail/{spuId}")
    public SpuDetail findSpuDetailBySpuId(@PathVariable("spuId") Long spuId){
        return spuService.findSpuDetailBySpuId(spuId);
    }

    /**
     * 根据商品spuid删除
     * @param spuId
     */
    @RequestMapping("deleteById/{spuId}")
    public void deleteById(@PathVariable("spuId") Long spuId){
        spuService.deleteById(spuId);
    }

    @RequestMapping("upOrDown/{saleable}/{spuId}")
    public void upOrDown(@PathVariable("saleable") Boolean saleable,
                         @PathVariable("spuId") Long spuId){
        System.out.println(saleable+"-"+spuId);
        spuService.upOrDown(saleable,spuId);
    }

    /**
     * 根据spuId查询spu信息
     *
     * @param spuId
     * @return
     */
    @RequestMapping("findSpuBySpuId")
    public Spu findSpuBySpuId(@RequestParam("spuId") Long spuId){
        return spuService.findSpuBySpuId(spuId);
    }

    @RequestMapping("findSpuVoBySpuId")
    public SpuVo findSpuVoBySpuId(@RequestParam("spuId") Long spuId){
      return   spuService.findSpuVoBySpuId(spuId);
    }
}


