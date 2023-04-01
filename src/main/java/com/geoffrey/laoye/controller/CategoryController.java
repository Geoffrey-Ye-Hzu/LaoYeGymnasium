package com.geoffrey.laoye.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.entity.Category;
import com.geoffrey.laoye.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
@SuppressWarnings("all")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    private R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询分类信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    private R<Page> page(int page,int pageSize){
        log.info("page = {},pageSize = {}", page, pageSize);
        //构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort);
        //进行分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    private R<String> delete(Long id){
        log.info("删除分类，id为{}",id);

        categoryService.remove(id);
        return R.success("分类信息删除成功");
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping
    private R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param type
     * @return
     */
    @GetMapping("/list")
    //这里传参是type不是json数据，封装在category里，所以不用加@RequestBody
    private R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
