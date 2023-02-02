package com.geoffrey.laoye.dto;

import com.geoffrey.laoye.entity.Setmeal;
import com.geoffrey.laoye.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
