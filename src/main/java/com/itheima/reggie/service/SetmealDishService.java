package com.itheima.reggie.service;

import com.itheima.reggie.entity.SetmealDish;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-04-02 12:08
 */
public interface SetmealDishService {

    void save(SetmealDish setmealDish);

    int removeBySetmealIds(List<Long> ids);
}
