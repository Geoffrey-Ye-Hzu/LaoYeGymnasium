package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.common.exception.CustomException;
import com.geoffrey.laoye.dto.SetmealDto;
import com.geoffrey.laoye.entity.Category;
import com.geoffrey.laoye.entity.Setmeal;
import com.geoffrey.laoye.entity.SetmealDish;
import com.geoffrey.laoye.mapper.SetmealMapper;
import com.geoffrey.laoye.service.CategoryService;
import com.geoffrey.laoye.service.SetmealDishService;
import com.geoffrey.laoye.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    //新增套餐，同时需要保存套餐和菜品的关联关系
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        //保存套餐和菜品的关联信息，操作setmeal_Dish,执行insert操作

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    //根据id删除套餐信息和套餐菜品关联信息
    @Override
    public void deleteByIdsWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Setmeal::getStatus, 1);
        queryWrapper.in(Setmeal::getId, ids);
        int count = this.count(queryWrapper);
        //判断该套餐是否正在售卖
        if(count>0){
            //正在售卖的套餐不能删除,抛出业务异常
            throw new CustomException("该套餐正在售卖中,不能删除");
        }
        //删除套餐表中的数据--setmeal
        this.removeByIds(ids);
        //删除关系表中的数据--setmeal_dish
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);

    }

    //根据id查询套餐信息和套餐菜品关系信息
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        Category category = categoryService.getById(setmealDto.getCategoryId());
        setmealDto.setCategoryName(category.getName());

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        LambdaUpdateWrapper<SetmealDish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(updateWrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long setmealId = setmealDto.getId();
        setmealDishes = setmealDishes.stream().map(item->{
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
