package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-25 21:14
 */
@Service
public class DishFlavorServiceImpl implements DishFlavorService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public int save(DishFlavor dishFlavor) {
        return dishFlavorMapper.insert(dishFlavor);
    }

    @Override
    public List<DishFlavor> listByDishId(Long id) {

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        //构造查询条件
        queryWrapper.eq(DishFlavor::getDishId, id);

        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(queryWrapper);
        return dishFlavors;
    }

    @Override
    public void removeByDishId(Long dishId) {
        //构造查询条件
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishId);
        //dishFlavorMapper.deleteById() 这里不能使用这个方法，因为这个方法只能根据主键删除，因为dishId不是主键
        dishFlavorMapper.delete(queryWrapper);
    }
}
