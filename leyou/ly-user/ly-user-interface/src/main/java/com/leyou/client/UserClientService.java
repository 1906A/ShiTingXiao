package com.leyou.client;

import com.leyou.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserClientService {

    /**
     * 用户登陆
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Boolean login(@RequestParam("username") String username,
                         @RequestParam("password") String password);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public User query(@RequestParam("username") String username,
                      @RequestParam("password") String password);
}
