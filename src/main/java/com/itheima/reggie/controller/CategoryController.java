package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-16 21:31
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.sava(category);
        log.info("添加分类成功");
        return R.success("添加分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        Page pageInfo = categoryService.page(page, pageSize);
        log.info("分类管理分页查询成功");
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> deleteById(long ids){
        categoryService.deleteById(ids);
        log.info("分类信息删除成功");
        return R.success("分类信息删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.update(category);
        log.info("分类修改成功");
        return R.success("分类修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        List<Category> categories = categoryService.list(category);
        log.info("菜品或套餐分类查询成功");
        return R.success(categories);
    }
}