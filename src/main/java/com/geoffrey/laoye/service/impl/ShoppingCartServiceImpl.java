package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.entity.ShoppingCart;
import com.geoffrey.laoye.mapper.ShoppingCartMapper;
import com.geoffrey.laoye.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
