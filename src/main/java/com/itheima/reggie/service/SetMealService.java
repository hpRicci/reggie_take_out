package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.SetmealDto;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-17 13:28
 */
public interface SetMealService {

    Integer count(Long ids);

    void saveWithDish(SetmealDto setMealDto);

    Page page(int page, int pageSize, String name);

    void removeWithSetmealDish(List<Long> ids);
}
