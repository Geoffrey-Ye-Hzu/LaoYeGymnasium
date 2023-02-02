package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.entity.OrderDetail;
import com.geoffrey.laoye.mapper.OrderDetailMapper;
import com.geoffrey.laoye.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
