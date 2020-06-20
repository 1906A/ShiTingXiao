package com.leyou.service;

import com.leyou.dao.UserMapper;
import com.leyou.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;


    public Boolean check(String data, Integer type) {
        User user = new User();
        Boolean result = false;
        //1: 判断校验的类型  type 1:用户名  2: 手机
        if (type == 1) {
            //判断用户名
            user.setUsername(data);
        } else if (type == 2) {
            //手机
            user.setPhone(data);
        }
        //2: 根据校验内容去数据库查询
        User user1 = userMapper.selectOne(user);
        if (user1 == null) {
            return true;
        }
        // 3: 用户名存 false  true  手机一样
        return result;
    }

    public void insertUser(User user) {
        //盐值
        String salt = UUID.randomUUID().toString().substring(0, 32);
        String psw = this.getPsw(user.getPassword(), salt);

        user.setPassword(psw);
        user.setCreated(new Date());

        user.setSalt(salt);
        userMapper.insert(user);
    }

    /**
     * 通过原生密码+盐值 生成MD5加密后的密码
     *
     * @param password
     * @param salt
     * @return
     */
    public String getPsw(String password, String salt) {
        //如何使用MD5加密
        String md5Hex = DigestUtils.md5Hex(password + salt);
        return md5Hex;

    }



    public User query(String username, String password) {
        User user = new User();
        //1:根据用户名查询
        user.setUsername(username);
        User user1 = userMapper.selectOne(user);
        if (user1 != null){
            //获取salt 盐值
            String salt = user1.getSalt();
            //调用方法生成加密密码
            String psw = this.getPsw(password, salt);
            //判断 生成的 和 查询的是否一致
            if (psw.equals(user1.getPassword())){
                return user1;
            }
        }
       return null;
    }
}
