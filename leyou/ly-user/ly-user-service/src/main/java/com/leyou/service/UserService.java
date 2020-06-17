package com.leyou.service;

import com.leyou.dao.UserMapper;
import com.leyou.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;


    public Boolean check(String data, Integer type) {
        User user = new User();
        Boolean result =false;
        //1: 判断校验的类型  type 1:用户名  2: 手机
        if (type == 1){
            //判断用户名
            user.setUsername(data);
        }else if (type == 2){
            //手机
            user.setPhone(data);
        }
        //2: 根据校验内容去数据库查询
        User user1 = userMapper.selectOne(user);
        if (user1 == null){
            return true;
        }
        // 3: 用户名存 false  true  手机一样
        return result;
    }
}
