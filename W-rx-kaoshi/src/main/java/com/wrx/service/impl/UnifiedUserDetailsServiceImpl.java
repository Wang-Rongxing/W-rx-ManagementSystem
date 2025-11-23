package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wrx.entity.Customer;
import com.wrx.entity.Employee;
import com.wrx.entity.SysUser;
import com.wrx.mapper.CustomerMapper;
import com.wrx.mapper.EmployeeMapper;
import com.wrx.mapper.SysUserMapper;
import com.wrx.util.LoginCustomer;
import com.wrx.util.LoginEmployee;
import com.wrx.util.LoginSysUSer;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 统一的用户详情服务实现类
 * 处理员工、客户和系统用户的登录验证
 */
@Service
public class UnifiedUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private EmployeeMapper employeeMapper;
    
    @Resource
    private CustomerMapper customerMapper;
    
    @Resource
    private SysUserMapper sysUserMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从username中提取角色信息（格式: role:username）
        String[] parts = username.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("用户名格式错误，应为: role:username");
        }
        
        String role = parts[0];
        String actualUsername = parts[1];
        
        // 根据不同的角色运行不同的代码
        return switch (role) {
            case "admin" ->
                // 系统管理员角色处理逻辑
                    handleAdminLogin(actualUsername);
            case "hotel" ->
                // 酒店员工角色处理逻辑
                    handleEmployeeLogin(actualUsername);
            case "customer" ->
                // 客户角色处理逻辑
                    handleCustomerLogin(actualUsername);
            default -> throw new RuntimeException("不支持的角色类型: " + role);
        };
    }
    
    /**
     * 处理系统管理员登录
     */
    private UserDetails handleAdminLogin(String username) {
        LambdaQueryWrapper<SysUser> sysUserWrapper = new LambdaQueryWrapper<>();
        sysUserWrapper.eq(SysUser::getAccount, username);
        SysUser sysUser = sysUserMapper.selectOne(sysUserWrapper);
        
        if (Objects.isNull(sysUser)) {
            throw new RuntimeException("管理员不存在: " + username);
        }
        
        // 管理员特有的业务逻辑
        System.out.println("管理员登录: " + username);
        
        LoginSysUSer loginSysUSer = new LoginSysUSer();
        loginSysUSer.setSysUser(sysUser);
        return loginSysUSer;
    }
    
    /**
     * 处理酒店员工登录
     */
    private UserDetails handleEmployeeLogin(String username) {
        LambdaQueryWrapper<Employee> employeeWrapper = new LambdaQueryWrapper<>();
        employeeWrapper.eq(Employee::getEmployeeId, username);
        Employee employee = employeeMapper.selectOne(employeeWrapper);
        
        if (Objects.isNull(employee)) {
            throw new RuntimeException("员工不存在: " + username);
        }
        
        // 员工特有的业务逻辑
        System.out.println("员工登录: " + username);
        
        LoginEmployee loginEmployee = new LoginEmployee();
        loginEmployee.setEmployee(employee);
        return loginEmployee;
    }
    
    /**
     * 处理客户登录
     */
    private UserDetails handleCustomerLogin(String username) {
        LambdaQueryWrapper<Customer> customerWrapper = new LambdaQueryWrapper<>();
        customerWrapper.eq(Customer::getCustomerId, username);
        Customer customer = customerMapper.selectOne(customerWrapper);
        
        if (Objects.isNull(customer)) {
            throw new RuntimeException("客户不存在: " + username);
        }
        
        // 客户特有的业务逻辑
        System.out.println("客户登录: " + username);
        
        LoginCustomer loginCustomer = new LoginCustomer();
        loginCustomer.setCustomer(customer);
        return loginCustomer;
    }
}