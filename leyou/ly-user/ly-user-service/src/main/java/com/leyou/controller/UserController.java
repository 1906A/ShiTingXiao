package com.leyou.controller;

import com.leyou.pojo.User;
import com.leyou.service.UserService;
import com.leyou.utils.CodeUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /**
     * 实现用户数据的校验 : 主要包含 手机号,用户名唯一校验
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public Boolean check(@PathVariable("data") String data,
                         @PathVariable("type") Integer type){
        System.out.println("check:"+data+"---"+type);

      Boolean result =  userService.check(data,type);


        return result;
    }

    /**
     * 根据用户手机号 生成随机验证码
     * @param phone
     */
    @PostMapping("/code")
    public void code(@RequestParam("phone") String phone){
        System.out.println("code:"+phone);

        //1:生成一个6位数字的随机码
        String code = CodeUtils.messageCode(6);
        //2: 调用短信服务发送验证码 调用 sms发送code
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);
       // amqpTemplate.convertAndSend("sms.exchanges","sms.send",map);
        //3:存入redis缓存
        stringRedisTemplate.opsForValue().set("lysms_"+phone,code,55, TimeUnit.MINUTES);

    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    @PostMapping("/register")
    public void register(@Valid User user, String code){

        System.out.println("用户注册:"+user.getUsername()+"----code:"+code);

        if (user != null){
            //1: 判断code 是否和redis 库中一致
            String redisCode = stringRedisTemplate.opsForValue().get("lysms_" + user.getPhone());
            if (redisCode.equals(code)){
                //相等
                userService.insertUser(user);
            }
        }
        //2: insert user
        //3: 密码处理  MD5加密 + 盐值
        //正常存库
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public User query(@RequestParam("username") String username,
                      @RequestParam("password") String password){
        System.out.println("查询用户:"+username+"---------"+password);
        return new User();
    }

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Boolean login(@RequestParam("username") String username,
                      @RequestParam("password") String password){
        Boolean result = userService.login(username,password);
        System.out.println(result);
        return result;
    }


}
