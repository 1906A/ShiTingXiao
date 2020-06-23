package com.leyou.controller;

import com.leyou.common.JwtUtils;
import com.leyou.common.UserInfo;
import com.leyou.config.JwtProperties;
import com.leyou.pojo.Sku;
import com.leyou.pojo.SkuVo;
import com.leyou.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    JwtProperties jwtProperties;



    public String prefix = "ly_carts_";


    /**
     * 加入购物车
     * @param sku
     */
    @RequestMapping("add")
    public void add(@CookieValue(value = "token") String token, @RequestBody SkuVo sku){

        UserInfo userInfo = this.getUserInfoByToken(token);
        if (userInfo != null){
           //添加redis
            BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(prefix + userInfo.getId());

            //先判断是否存在
            if (hashOps.hasKey(sku.getId())){
                //存在  先拿到
                String redisSkuString = (String) hashOps.get(sku.getId().toString());
                //解析成为实体类
                SkuVo redisSku = JsonUtils.parse(redisSkuString, SkuVo.class);
                //修改购物车在redis中的num数量值
                redisSku.setNum(redisSku.getNum()+sku.getNum());


                //重新存放到redis
                //转换成为json串
                String serialize = JsonUtils.serialize(redisSku);
                hashOps.put(sku.getId(),serialize);

            }else {
                hashOps.put(sku.getId(),sku);
            }

        }



    }


    @RequestMapping("update")
    public void update(@RequestBody Sku sku){

    }

    @RequestMapping("delete")
    public void update(@RequestParam("id") Long id){

    }

    @RequestMapping("query")
    public void query(){

    }



    public UserInfo getUserInfoByToken(String token){
        UserInfo userInfo = new UserInfo();
        try {
            userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }


}
