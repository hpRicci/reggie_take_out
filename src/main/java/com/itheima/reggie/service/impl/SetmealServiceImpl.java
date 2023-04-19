package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.SetMeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.exception.CustomException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetMealService;
import com.itheima.reggie.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-17 13:29
 */
@Service
public class SetmealServiceImpl implements SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 在分类管理中的删除功能，用于查看套餐分类是否关联了套餐
     * @param id
     * @return
     */
    @Override
    public Integer count(Long id) {
        LambdaQueryWrapper<SetMeal> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(SetMeal::getCategoryId, id);
        return setmealMapper.selectCount(dishLambdaQueryWrapper);
    }

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setMealDto) {
        //保存套餐基本信息
        setmealMapper.insert(setMealDto);

        //获取setmealDishes
        List<SetmealDish> setmealDishes = setMealDto.getSetmealDishes();

        //保存套餐和菜品的关系
        for(SetmealDish setmealDish : setmealDishes){
            //设置套餐id
            setmealDish.setSetmealId(setMealDto.getId());
            //保存
            setmealDishService.save(setmealDish);
        }
    }

    @Override
    public Page page(int page, int pageSize, String name) {
        //构建分页对象
        Page<SetMeal> setMealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        //构建查询条件
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, SetMeal::getName, name);
        queryWrapper.orderByDesc(SetMeal::getUpdateTime);

        //查询
        setmealMapper.selectPage(setMealPage, queryWrapper);

        //拷贝对象
        BeanUtils.copyProperties(setMealPage, setmealDtoPage, "records");

        //更新records为List<SetmealDto>
        List<SetMeal> setMeals = setMealPage.getRecords();
        List<SetmealDto> setmealDtos = new ArrayList<>();
        for(SetMeal setMeal : setMeals){

            SetmealDto setmealDto = new SetmealDto();

            //拷贝对象
            BeanUtils.copyProperties(setMeal, setmealDto);

            //为setmealDto设置categoryName
            Long categoryId = setMeal.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            setmealDto.setCategoryName(category.getName());

            //添加到setmealDtos
            setmealDtos.add(setmealDto);
        }
        setmealDtoPage.setRecords(setmealDtos);

        return setmealDtoPage;
    }

    @Override
    @Transactional
    public void removeWithSetmealDish(List<Long> ids) {
        //查询套餐状态是否可以删除
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetMeal::getId, ids);
        queryWrapper.eq(SetMeal::getStatus, 1);
        int count = setmealMapper.selectCount(queryWrapper);

        //如果不可以删除，抛出异常
        if(count > 0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除, 先删除套餐菜品，再删除套餐
        setmealDishService.removeBySetmealIds(ids);
        setmealMapper.deleteBatchIds(ids);
    }

}
