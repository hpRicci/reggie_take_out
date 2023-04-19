package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-03-25 21:10
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlavor(dishDto);
        log.info("菜品添加成功");
        return R.success("菜品添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page dishPage = dishService.page(page, pageSize, name);
        log.info("菜品管理分页查询成功");
        return R.success(dishPage);
    }

    @GetMapping("{id}")
    public R<DishDto> getById(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        log.info("菜品详情查询成功");
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);
        log.info("菜品修改成功");
        return R.success("菜品修改成功");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        List<Dish> dishList = dishService.list(dish);
        log.info("根据菜品分类查询成功");
        return R.success(dishList);
    }

}
