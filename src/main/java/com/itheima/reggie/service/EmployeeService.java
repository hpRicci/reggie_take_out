package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.Employee;

/**
 * @author 陈万三
 * @create 2023-03-05 16:47
 */
public interface EmployeeService {

    public Employee login(Employee employee);

    public boolean save(Employee employee);

    public Page page(long page, long pageSize, String name);

    public boolean updateById(Employee employee);

    public Employee selectById(long id);
}
