package com.geoffrey.laoye.dto;

import com.geoffrey.laoye.entity.Dish;
import com.geoffrey.laoye.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
//当一个父类实现Serializable接口后,他的子类都将自动的实现序列化,所以该dto实体类不用实现Serializable接口
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
