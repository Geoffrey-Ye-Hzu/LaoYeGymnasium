package com.geoffrey.laoye.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoffrey.laoye.entity.Employee;
import com.geoffrey.laoye.mapper.EmployeeMapper;
import com.geoffrey.laoye.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{
}
