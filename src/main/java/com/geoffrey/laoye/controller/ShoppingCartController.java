package com.geoffrey.laoye.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.geoffrey.laoye.common.BaseContext;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.entity.ShoppingCart;
import com.geoffrey.laoye.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
@SuppressWarnings("all")

public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车菜品或套餐的数量
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    private R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        //设置用户id，指定当前是哪个用户的购物车数据
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        //Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        //添加到购物车的是套餐
        queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (shoppingCartServiceOne != null) {
            //如果已经存在，就在原来数量基础上加一
            shoppingCartServiceOne.setNumber(shoppingCartServiceOne.getNumber() + 1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        } else {
            //如果不存在，则直接添加在购物车中，数量默认为一
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne = shoppingCart;
        }
        return R.success(shoppingCartServiceOne);
    }

    /**
     * 减少购物车项目数量
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    private R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();
        //设置用户id，指定当前是哪个用户的购物车数据
        shoppingCart.setUserId(currentId);
        //查询当前项目是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        //添加到购物车的是套餐
        queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);


        shoppingCartServiceOne.setNumber(shoppingCartServiceOne.getNumber() - 1);
        shoppingCartService.updateById(shoppingCartServiceOne);

        //数量只剩下1再减则从购物车中删除
        if (shoppingCartServiceOne.getNumber() == 0) {
            LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
            lambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);
            shoppingCartService.remove(lambdaQueryWrapper);
        }

        return R.success(shoppingCartServiceOne);
    }

    /**
     * 展示菜品或套餐的数量到购物车中
     *
     * @return
     */
    @GetMapping("/list")
    private R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    private R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }
}
