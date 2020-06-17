package com.leyou.controller;

import com.leyou.pojo.User;
import com.leyou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;


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
    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    @PostMapping("/register")
    public void register(User user,String code){

        System.out.println("用户注册:"+user.getUsername()+"----code:"+code);
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
}
