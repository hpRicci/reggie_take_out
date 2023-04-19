package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 陈万三
 * @create 2023-04-02 11:48
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setMealDto) {
        setMealService.saveWithDish(setMealDto);
        log.info("新增套餐成功");
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page setMealPage = setMealService.page(page, pageSize, name);
        log.info("套餐分页查询成功");
        return R.success(setMealPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setMealService.removeWithSetmealDish(ids);
        log.info("删除套餐成功");
        return R.success("删除套餐成功");
    }
}
