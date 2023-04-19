package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-17 13:29
 */
public interface DishService {

    Integer count(long ids);

    void saveWithFlavor(DishDto dishDto);

    Page page(int page, int pageSize, String name);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    List<Dish> list(Dish dish);
}
