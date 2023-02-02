package com.geoffrey.laoye.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.dto.DishDto;
import com.geoffrey.laoye.entity.Category;
import com.geoffrey.laoye.entity.Dish;
import com.geoffrey.laoye.entity.DishFlavor;
import com.geoffrey.laoye.service.CategoryService;
import com.geoffrey.laoye.service.DishFlavorService;
import com.geoffrey.laoye.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    //传参是json数据记得添加@RequestBody，前端回传的json数据需要RequestBody反序列化到我们定义的实体对象中
    private R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    private R<Page> page(int page, int pageSize, String name) {
        log.info("page= {},pageSize = {},name = {}", page, pageSize, name);
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);


        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    private R<DishDto> get(@PathVariable Long id) {
        log.info("id：{}", id);
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 更新菜品信息，同时更新对应的口味信息
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    private R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品信息成功");
    }

    /**
     * 启售和停售操作（可批量操作）
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    private R<String> status(@PathVariable Integer status, Long[] ids) {
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(ids != null, Dish::getId, ids);
        updateWrapper.set(Dish::getStatus, status);
        dishService.update(updateWrapper);
        return R.success("操作成功");
    }

    /**
     * 根据数组ids(批量)删除菜品信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    //别忘记集合需要添加@RequestParam注解!!!!
    private R<String> delete(@RequestParam List<Long> ids) {
        for (Long id : ids) {
            log.info("id值为:{}", id);
        }
        dishService.deleteByIdsWithFlavor(ids);
        return R.success("删除菜品信息成功");
    }


    /**
     * 根据条件来查询对应的菜品数据
     *
     * @param dish
     * @return
     */
    //@GetMapping("/list")
    //private R<List<Dish>> list(Dish dish){
    //    LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
    //    queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
    //    //查询状态为1(启售状态)的菜品
    //    queryWrapper.eq(Dish::getStatus,1);
    //    queryWrapper.eq(StringUtils.isNotEmpty(dish.getName()),Dish::getName,dish.getName());
    //    queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    //    List<Dish> list = dishService.list(queryWrapper);
    //    return R.success(list);
    //}
    @GetMapping("/list")
    private R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //查询状态为1(启售状态)的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            lambdaQueryWrapper.orderByDesc(DishFlavor::getUpdateTime);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());


        return R.success(dishDtoList);
    }
}
