package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-17 13:30
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 在分类管理中的删除功能，用于查看菜品分类是否关联了菜品
     * @param id
     * @return
     */
    @Override
    public Integer count(long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        return dishMapper.selectCount(queryWrapper);
    }

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        dishMapper.insert(dishDto);

        //获取菜品Id
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            //将菜品Id设置在口味对象上, 使得菜品表与口味表关联
            flavor.setDishId(dishId);
            //同时将口味数据存储在口味表中
            dishFlavorService.save(flavor);
        }

    }

    @Override
    public Page page(int page, int pageSize, String name) {

        //构造分页构造器
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishMapper.selectPage(dishPage, queryWrapper);

        /*
            将dishPage拷贝到dishDto(除了records)
            因为records中的是List<Dish>, 而响应页面需要categoryName, 但Dish中没有categoryName, 所以需要借助DishDto。
        */
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        //取dishPage中的records
        List<Dish> dishes = dishPage.getRecords();

        List<DishDto> records = new ArrayList<>(); //作为dishDtoPage中的records

        for (Dish dish : dishes) {

            DishDto dishDto = new DishDto();

            //拷贝对象
            BeanUtils.copyProperties(dish, dishDto); //此时dishDto只差CategoryName属性为null

            //为dishDto设置categoryName属性
            Long categoryId = dish.getCategoryId();
            //根据categoryId查询得到category对象, 从而得到categoryName属性
            Category category = categoryMapper.selectById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);

            //将dishDto添进List<DishDto>
            records.add(dishDto);
        }

        //给dishDtoPage.records赋值
        dishDtoPage.setRecords(records);

        return dishDtoPage;
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish != null) {
            DishDto dishDto = new DishDto();
            //拷贝对象
            BeanUtils.copyProperties(dish, dishDto);
            //根据菜品Id得到口味集合
            List<DishFlavor> flavors = dishFlavorService.listByDishId(id);

            dishDto.setFlavors(flavors);
            return dishDto;
        }
        return null;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品的基本信息到菜品表dish
        dishMapper.updateById(dishDto);

        //获取菜品Id
        Long dishId = dishDto.getId();
        //根据菜品Id删除口味表中的数据
        dishFlavorService.removeByDishId(dishId);

        //重新添加修改后得口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            //将菜品Id设置在口味对象上, 使得菜品表与口味表关联
            flavor.setDishId(dishId);
            //同时将口味数据存储在口味表中
            dishFlavorService.save(flavor);
        }
    }

    @Override
    public List<Dish> list(Dish dish) {
        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishes = dishMapper.selectList(queryWrapper);
        return dishes;
    }


}
