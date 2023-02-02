package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.entity.User;
import com.geoffrey.laoye.mapper.UserMapper;
import com.geoffrey.laoye.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
