package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈万三
 * @create 2023-03-05 16:49
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.对页面提交的password进行md5加密处理
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        //2.根据页面提交的的username查询数据库
        Employee emp = employeeService.login(employee);

        //3.如果没有查询到则返回登录失败
        if (emp == null){
            return R.error("登录失败");
        }

        //4.进行密码比对, 如果不一致返回登录失败
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }

        //5.查看员工状态, 如果为禁用状态, 则返回员工已禁用
        if (emp.getStatus() == 0){
            return  R.error("员工已禁用");
        }

        //登录成功, 将员工id存入Session并放回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());

        log.info("登录成功");
        return R.success(emp);
    }

    @PostMapping("logout")
    public R<String> logout(HttpServletRequest httpServletRequest){
        //清楚Session中保存的
        httpServletRequest.removeAttribute("employee");
        log.info("退出成功");
        return R.success("退出成功");
    }

    /**
     * 添加员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){

        System.out.println(employee.toString());

        //1.初始化实体类数据
        //初始化密码为123456, 加密形式
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //初始化其他数据
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        //获取当前登录用户的id
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        //2.添加
        employeeService.save(employee);
        log.info("员工添加成功");
        return R.success("添加成功");
    }

    /**
     * 页面查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(long page, long pageSize, String name){

        Page pageInfo = employeeService.page(page, pageSize, name);
        log.info("员工分页查询成功");
        return R.success(pageInfo);
    }

    /**
     * 更新用户信息
     * 问题：在页面中, js对long型数据进行处理时丢失精度, 导致提交的id和数据库id不一致
     *      解决方法: 服务器给页面响应数据时进行处理, 将long型数据统一转为string字符串(使用消息转换器)
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        //获取当前员工Id
//        Long empId = (Long) request.getSession().getAttribute("employee");
        //更新更改时间与更改用户
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);

        employeeService.updateById(employee);
        log.info("员工更新成功");
        return R.success("员工更新成功");
    }

    @GetMapping("/{id}")
    public R<Employee> selectById(@PathVariable long id){
        Employee employee = employeeService.selectById(id);
        if (employee != null){
            log.info("根据id查询成功");
            return R.success(employee);
        }
        log.info("没有查询到该员工");
        return R.error("没有查询到该用户");
    }

}
