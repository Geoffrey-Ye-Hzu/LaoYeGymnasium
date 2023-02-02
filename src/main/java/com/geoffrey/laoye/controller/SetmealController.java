package com.geoffrey.laoye.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.dto.DishDto;
import com.geoffrey.laoye.dto.SetmealDto;
import com.geoffrey.laoye.entity.Category;
import com.geoffrey.laoye.entity.Dish;
import com.geoffrey.laoye.entity.Setmeal;
import com.geoffrey.laoye.entity.SetmealDish;
import com.geoffrey.laoye.service.CategoryService;
import com.geoffrey.laoye.service.DishService;
import com.geoffrey.laoye.service.SetmealDishService;
import com.geoffrey.laoye.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    private R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 分页查询套餐信息
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    private R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Category category = categoryService.getById(setmealDto.getCategoryId());
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        log.debug("看看setmealdto的结果是：{}", setmealDtoPage);
        return R.success(setmealDtoPage);
    }

    /**
     * 根据id删除套餐信息和套餐菜品关联信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    //别忘记集合需要添加@RequestParam注解!!!!
    private R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteByIdsWithDish(ids);
        return R.success("删除套餐成功");
    }

    /**
     * 根据id查询套餐信息和套餐菜品关系信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    private R<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        log.info("setmealDto的信息为：{}", setmealDto);
        return R.success(setmealDto);
    }

    /**
     * 修改套餐信息和套餐菜品关系信息
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    private R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("setmealDto的信息为：{}", setmealDto);
        setmealService.updateWithDish(setmealDto);
        return R.success("套餐修改成功");
    }

    /**
     * 根据ids修改销售状态（可批量）
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    private R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Setmeal::getId, ids);
        updateWrapper.set(Setmeal::getStatus, status);
        setmealService.update(updateWrapper);
        return R.success("修改状态成功");
    }

    /**
     * 根据条件查询套餐数据
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    private R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, 1);
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 获取套餐的全部菜品
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> showSetmealDish(@PathVariable Long id) {
        //条件构造器
        LambdaQueryWrapper<SetmealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //手里的数据只有setmealId
        dishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        //查询数据
        List<SetmealDish> records = setmealDishService.list(dishLambdaQueryWrapper);
        List<DishDto> dtoList = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //copy数据
            BeanUtils.copyProperties(item,dishDto);
            //查询对应菜品id
            Long dishId = item.getDishId();
            //根据菜品id获取具体菜品数据，这里要自动装配 dishService
            Dish dish = dishService.getById(dishId);
            //其实主要数据是要那个图片，不过我们这里多copy一点也没事
            BeanUtils.copyProperties(dish,dishDto);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dtoList);
    }
}
