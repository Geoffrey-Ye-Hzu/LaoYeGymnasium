package com.geoffrey.laoye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geoffrey.laoye.dto.DishDto;
import com.geoffrey.laoye.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish,dish_flavor
    @Transactional
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    @Transactional
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味信息
    @Transactional
    public void updateWithFlavor(DishDto dishDto);

    //根据数组ids(批量)删除菜品信息
    @Transactional
    public void deleteByIdsWithFlavor(List<Long> ids);
}
