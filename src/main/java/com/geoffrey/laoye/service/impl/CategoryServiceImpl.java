package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.common.exception.CustomException;
import com.geoffrey.laoye.entity.Category;
import com.geoffrey.laoye.entity.Dish;
import com.geoffrey.laoye.entity.Setmeal;
import com.geoffrey.laoye.mapper.CategoryMapper;
import com.geoffrey.laoye.service.CategoryService;
import com.geoffrey.laoye.service.DishService;
import com.geoffrey.laoye.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int countD = dishService.count(dishLambdaQueryWrapper);
        if (countD>0){
            //已经关联菜品了，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除噢！");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int countS = setmealService.count(setmealLambdaQueryWrapper);
        if (countS>0){
            //已经关联套餐了，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除噢！");
        }

        //都没有关联则正常删除
        super.removeById(id);
    }
}
