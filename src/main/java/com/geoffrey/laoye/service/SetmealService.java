package com.geoffrey.laoye.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.dto.SetmealDto;
import com.geoffrey.laoye.entity.Category;
import com.geoffrey.laoye.entity.Setmeal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐，同时需要保存套餐和菜品的关联关系
    @Transactional
    public void saveWithDish(SetmealDto setmealDto);

    //根据id删除套餐信息和套餐菜品关联信息
    @Transactional
    public void deleteByIdsWithDish(List<Long> ids);

    //根据id查询套餐信息和套餐菜品关系信息
    @Transactional
    public SetmealDto getByIdWithDish(Long id);

    //修改套餐信息和套餐菜品关系信息
    @Transactional
    public void updateWithDish(SetmealDto setmealDto);
}
