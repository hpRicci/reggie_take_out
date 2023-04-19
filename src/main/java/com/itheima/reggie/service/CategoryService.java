package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.controller.R;
import com.itheima.reggie.entity.Category;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-16 21:30
 */
public interface CategoryService {

    int sava(Category category);

    Page page(int page, int pageSize);

    int deleteById(Long id);

    int update(Category category);

    List<Category> list(Category category);

    Category selectById(Long id);

}
