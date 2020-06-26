package com.leyou.controller;

import com.leyou.common.JwtUtils;
import com.leyou.common.UserInfo;
import com.leyou.config.JwtProperties;
import com.leyou.pojo.SkuVo;
import com.leyou.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
@Api("购物车的服务接口")
public class CartController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    JwtProperties jwtProperties;


    public String prefix = "ly_carts_";

    public String prefixselectedSku = "ly_carts_selectedSku";


    /**
     * 添加购物车
     *
     * @param token
     * @param sku
     */
    @PostMapping("add")
    @ApiOperation(value = "购物车添加保存到redis",notes = "购物车添加")
    @ApiImplicitParam(name = "sku",required = true,value = "结算页选择的sku串")
    public void add(@CookieValue(value = "token") String token, @RequestBody SkuVo sku) {

        System.out.println(sku.getId());
        UserInfo userInfo = this.getUserInfoByToken(token);
        if (userInfo != null) {
            //添加redis
            BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(prefix + userInfo.getId());

            //先判断是否存在
            if (hashOps.hasKey(sku.getId() + "")) {
                //存在  先拿到
                String redisSkuString = (String) hashOps.get(sku.getId().toString());
                //解析成为实体类
                SkuVo redisSku = JsonUtils.parse(redisSkuString, SkuVo.class);
                //修改购物车在redis中的num数量值
                redisSku.setNum(redisSku.getNum() + sku.getNum());


                //重新存放到redis
                //转换成为json串
                String serialize = JsonUtils.serialize(redisSku);
                hashOps.put(sku.getId() + "", serialize);


                //当前操作的sku单独存放到redis中
                stringRedisTemplate.boundValueOps(prefixselectedSku + userInfo.getId()).set(JsonUtils.serialize(sku));
            } else {
                //第一次存放redis
                hashOps.put(sku.getId() + "", JsonUtils.serialize(sku));

                //当前操作的sku单独存放到redis中
                stringRedisTemplate.boundValueOps(prefixselectedSku + userInfo.getId()).set(JsonUtils.serialize(sku));
            }
        }
    }

    /**
     * 从redis中获取当前操作的sku
     *
     * @param token
     * @return
     */
    @PostMapping("selectedSku")
    public SkuVo selectedSku(@CookieValue(value = "token") String token) {
        UserInfo userInfo = this.getUserInfoByToken(token);
        //从redis中获取最新的sku
        String s = stringRedisTemplate.boundValueOps(prefixselectedSku + userInfo.getId()).get();
        SkuVo skuVo = JsonUtils.parse(s, SkuVo.class);
        return skuVo;

    }

    /**
     * 修改购物车
     *
     * @param token
     * @param skuVo
     */
    @PostMapping("update")
    public void update(@CookieValue(value = "token") String token, @RequestBody SkuVo skuVo) {
        UserInfo userInfo = this.getUserInfoByToken(token);
        if (userInfo != null) {
            //添加redis
            BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(prefix + userInfo.getId());

            //先判断是否存在
            if (hashOps.hasKey(skuVo.getId() + "")) {
                //存在  先拿到
                String redisSkuString = (String) hashOps.get(skuVo.getId().toString());
                //解析成为实体类
                SkuVo redisSku = JsonUtils.parse(redisSkuString, SkuVo.class);
                //修改购物车在redis中的num数量值
                redisSku.setNum(skuVo.getNum());


                //重新存放到redis
                //转换成为json串
                String serialize = JsonUtils.serialize(redisSku);
                hashOps.put(skuVo.getId() + "", serialize);

            } else {
                //第一次存放redis
                hashOps.put(skuVo.getId() + "", JsonUtils.serialize(skuVo));
            }

        }
    }

    /**
     * 根据skuId删除购物车
     *
     * @param token
     * @param id
     */
    @PostMapping("delete")
    public void delete(@CookieValue(value = "token") String token, @RequestParam("id") Long id) {
        UserInfo userInfo = this.getUserInfoByToken(token);
        if (userInfo != null) {
            //添加redis
            BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(prefix + userInfo.getId());

            //先判断是否存在
            hashOps.delete(id+"");
        }
    }

    /**
     * 查询购物车
     *
     * @param token
     * @return
     */
    @PostMapping("query")
    public List<SkuVo> query(@CookieValue(value = "token") String token) {
        UserInfo userInfo = this.getUserInfoByToken(token);
        List<SkuVo> skuVoList = new ArrayList<>();
        if (userInfo != null) {
            //添加redis
            BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(prefix + userInfo.getId());

            Map<Object, Object> map = hashOps.entries();

            map.keySet().forEach(key -> {
                SkuVo skuVo = JsonUtils.parse(hashOps.get(key).toString(), SkuVo.class);
                skuVoList.add(skuVo);
            });
        }
        return skuVoList;
    }


    public UserInfo getUserInfoByToken(String token) {
        UserInfo userInfo = new UserInfo();
        try {
            userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }


}
