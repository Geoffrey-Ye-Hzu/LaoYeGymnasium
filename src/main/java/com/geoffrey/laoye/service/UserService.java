package com.geoffrey.laoye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geoffrey.laoye.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


public interface UserService extends IService<User> {

    @Transactional
    public User saveRegister(Map map);
}
