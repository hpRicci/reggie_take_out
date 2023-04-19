package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈万三
 * @create 2023-03-05 16:48
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(Employee employee) {
        //2.根据页面提交的的username查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeMapper.selectOne(lqw);

        return emp;
    }

    @Override
    public boolean save(Employee employee) {
        int insert = employeeMapper.insert(employee);
        return insert != 0;
    }

    @Override
    public Page page(long page, long pageSize, String name) {

        //构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

        employeeMapper.selectPage(pageInfo, QueryWrapper);
        return pageInfo;
    }

    @Override
    public boolean updateById(Employee employee) {

        employeeMapper.updateById(employee);
        return false;
    }

    @Override
    public Employee selectById(long id) {
        Employee employee = employeeMapper.selectById(id);
        return employee;
    }


}
