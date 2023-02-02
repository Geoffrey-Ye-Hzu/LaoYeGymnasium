package com.geoffrey.laoye.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.geoffrey.laoye.common.R;
import com.geoffrey.laoye.entity.Employee;
import com.geoffrey.laoye.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@SuppressWarnings("all")

public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录操作
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    private R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到则返回登录失败的结果
        if (emp == null) {
            return R.error("登录失败,该用户名不存在");
        }
        //4.密码比对，如果不一致则返回登录失败的结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败，密码输入错误");
        }
        //5.查看员工状态，如果已为禁用状态，则返回员工已禁用的结果
        if (emp.getStatus() == 0) {
            return R.error("账户已被禁用");
        }
        //6.登录成功，将员工的id存入Session并返回登录成功的结果
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 登出操作
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    private R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping
    private R<String> save(HttpServletRequest httpServletRequest, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());

        //设置初始密码为123456，需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //获取当前登录用户的id(因为有一些字段属性可以定位是哪个用户进行的操作）
        //Long empId = (Long) httpServletRequest.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页查询员工信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    private R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(name)){
            //细节：去掉前后空格
            name = name.trim();
        }
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //这里的排序条件最好是创建时间,不然后面修改时会导致员工列表发生变化
        queryWrapper.orderByDesc(Employee::getCreateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    private R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        //js对long型数据进行处理时丢失精度，导致提交的id和数据库中的id不一致，从而修改失败
        //解决：在服务端给页面响应json数据时进行处理，将long型数据统一转为String字符串
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setUpdateUser(empId);
        //employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    private R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息.....");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到对应的信息");
    }
}
