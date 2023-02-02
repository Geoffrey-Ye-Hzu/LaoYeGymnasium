package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.entity.DishFlavor;
import com.geoffrey.laoye.mapper.DishFlavorMapper;
import com.geoffrey.laoye.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
