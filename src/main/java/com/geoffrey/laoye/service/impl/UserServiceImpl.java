package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.entity.AddressBook;
import com.geoffrey.laoye.entity.User;
import com.geoffrey.laoye.mapper.UserMapper;
import com.geoffrey.laoye.service.AddressBookService;
import com.geoffrey.laoye.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public User saveRegister(Map map) {
        //获取手机号
        String phone = map.get("phone").toString();
        //获取密码
        String password = map.get("password").toString();
        //判断当前手机号对应的用户是否是新用户，若是新用户则自动完成注册（存储该用户信息）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = this.getOne(queryWrapper);
        log.warn("user是空的嘛？===》》{}", user);
        if (user != null) {
            return null;
        }
        //新用户，完成信息录入（注册）
        user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setName("用户" + phone);
        user.setStatus(1);
        this.save(user);
        //log.debug("user.getId()= {}", user.getId());


        return user;
    }
}
