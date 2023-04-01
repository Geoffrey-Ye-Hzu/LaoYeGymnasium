package com.geoffrey.laoye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geoffrey.laoye.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    public void submit(Orders orders);

}
