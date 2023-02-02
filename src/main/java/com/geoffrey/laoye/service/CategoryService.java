package com.geoffrey.laoye.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.geoffrey.laoye.entity.Category;


public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
