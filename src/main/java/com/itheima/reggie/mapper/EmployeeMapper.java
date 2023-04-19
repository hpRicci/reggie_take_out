package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 陈万三
 * @create 2023-03-05 16:44
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
