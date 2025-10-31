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
        // 尝试作为员工登录
        LambdaQueryWrapper<Employee> employeeWrapper = new LambdaQueryWrapper<>();
        employeeWrapper.eq(Employee::getEmployeeId, username);
        Employee employee = employeeMapper.selectOne(employeeWrapper);
        if (!Objects.isNull(employee)) {
            LoginEmployee loginEmployee = new LoginEmployee();
            loginEmployee.setEmployee(employee);
            return loginEmployee;
        }
        
        // 尝试作为客户登录
        LambdaQueryWrapper<Customer> customerWrapper = new LambdaQueryWrapper<>();
        customerWrapper.eq(Customer::getCustomerId, username);
        Customer customer = customerMapper.selectOne(customerWrapper);
        if (!Objects.isNull(customer)) {
            LoginCustomer loginCustomer = new LoginCustomer();
            loginCustomer.setCustomer(customer);
            return loginCustomer;
        }
        
        // 尝试作为系统用户登录
        LambdaQueryWrapper<SysUser> sysUserWrapper = new LambdaQueryWrapper<>();
        sysUserWrapper.eq(SysUser::getAccount, username);
        SysUser sysUser = sysUserMapper.selectOne(sysUserWrapper);
        if (!Objects.isNull(sysUser)) {
            LoginSysUSer loginSysUSer = new LoginSysUSer();
            loginSysUSer.setSysUser(sysUser);
            return loginSysUSer;
        }
        
        // 如果所有类型都找不到用户，则抛出异常
        throw new RuntimeException("用户名错误或不存在");
    }
}