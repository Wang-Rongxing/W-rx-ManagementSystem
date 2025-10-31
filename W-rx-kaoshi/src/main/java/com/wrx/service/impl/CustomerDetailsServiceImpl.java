//package com.wrx.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.wrx.entity.Customer;
//import com.wrx.entity.Employee;
//import com.wrx.mapper.CustomerMapper;
//import com.wrx.mapper.EmployeeMapper;
//import com.wrx.util.LoginCustomer;
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
//public class CustomerDetailsServiceImpl implements UserDetailsService {
//    @Resource
//    private CustomerMapper customerMapper;
//    //用登录名查询user，封装到UserDetails（LoginUser）
//    @Override
//    public UserDetails loadUserByUsername(String jobId) throws UsernameNotFoundException {
//        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Customer::getCustomerId,jobId);
//        Customer customer = customerMapper.selectOne(wrapper);
//        if (Objects.isNull(customer)) {
//            throw new RuntimeException("工号错误或不存在");
//        }
//        LoginCustomer loginCustomer = new LoginCustomer();
//        loginCustomer.setCustomer(customer);
//        return loginCustomer;
//    }
//}
