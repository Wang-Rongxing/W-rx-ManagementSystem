//package com.wrx.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.wrx.entity.Employee;
//import com.wrx.mapper.EmployeeMapper;
//import com.wrx.util.LoginEmployee;
//import jakarta.annotation.Resource;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
///*
//*加载需要认证user
// */
//@Service
//public class EmployeeDetailsServiceImpl implements UserDetailsService {
//    @Resource
//    private EmployeeMapper employeeMapper;
//    //用登录名查询user，封装到UserDetails（LoginUser）
//    @Override
//    public UserDetails loadUserByUsername(String jobId) throws UsernameNotFoundException {
//        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Employee::getEmployeeId,jobId);
//        Employee employee = employeeMapper.selectOne(wrapper);
//        if (Objects.isNull(employee)) {
//            throw new RuntimeException("工号错误或不存在");
//        }
//        LoginEmployee loginEmployee = new LoginEmployee();
//        loginEmployee.setEmployee(employee);
//        return loginEmployee;
//    }
//}
