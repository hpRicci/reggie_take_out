package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.controller.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.exception.BusinessException;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-16 21:31
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public int sava(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    public Page page(int page, int pageSize) {
        //构造分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        categoryMapper.selectPage(pageInfo, queryWrapper);
        return pageInfo;
    }

    @Override
    public int deleteById(Long id) {
        //查询当前分类是否关联了菜品, 如果关联了, 则抛出一个业务异常
        int dishCount = dishService.count(id);
        if (dishCount > 0){
            //抛自定义业务异常
            throw new BusinessException("当前分类已关联菜品, 删除失败");
        }

        //查询当前分类是否关联了套餐, 如果关联了, 则抛出一个业务异常
        int setMealCount = setMealService.count(id);
        if (setMealCount > 0){
            //抛自定义业务异常
            throw new BusinessException("当前分类已关联套餐, 删除失败");
        }
        return categoryMapper.deleteById(id);
    }

    @Override
    public int update(Category category) {
        return categoryMapper.updateById(category);
    }

    @Override
    public List<Category> list(Category category) {
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);

        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return categories;
    }

    @Override
    public Category selectById(Long id) {
        return categoryMapper.selectById(id);
    }

}
