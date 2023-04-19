package com.itheima.reggie.service;

import com.itheima.reggie.entity.DishFlavor;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-25 21:13
 */
public interface DishFlavorService {

    int save(DishFlavor dishFlavor);

    List<DishFlavor> listByDishId(Long id);

    void removeByDishId(Long dishId);
}
