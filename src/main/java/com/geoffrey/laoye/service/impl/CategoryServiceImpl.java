package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.common.exception.CustomException;
import com.geoffrey.laoye.entity.Category;
import com.geoffrey.laoye.entity.Combo;
import com.geoffrey.laoye.mapper.CategoryMapper;
import com.geoffrey.laoye.service.CategoryService;
import com.geoffrey.laoye.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private ComboService comboService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Combo> comboLambdaQueryWrapper = new LambdaQueryWrapper<>();
        comboLambdaQueryWrapper.eq(Combo::getCategoryId,id);
        int countD = comboService.count(comboLambdaQueryWrapper);
        if (countD>0){
            //已经关联菜品了，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除噢！");
        }

        //都没有关联则正常删除
        super.removeById(id);
    }
}
