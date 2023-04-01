package com.geoffrey.laoye.controller;

import com.geoffrey.laoye.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单明细
 */
@RestController
@RequestMapping
@Slf4j
@SuppressWarnings("all")

public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;


}
