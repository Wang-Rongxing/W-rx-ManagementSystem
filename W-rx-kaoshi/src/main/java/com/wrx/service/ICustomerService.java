package com.wrx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
import com.wrx.entity.Employee;
import com.wrx.entity.SysUser;

import java.util.Map;

public interface ICustomerService extends IService<Customer> {
    
    LoginUserDto login(Customer customer);
    
    boolean register(Customer customer);
    
    Page<Customer> selectByPage(Customer customer, int pageNum, int pageSize);
    
    Map<String, Object> selectAllUser(Customer customer, int pageIndex, int pageSize);

    Map<String, Object> selectCustomerByIdOrName(Customer customer);
    
    boolean insertUser(Customer customer);
    
    Customer getCustomerById(String customerId);
    
    Customer getCustomerById(Integer Id);
    
    Map<String, Object> getHistory(String customerId, int page, int pageSize);
    
    Customer getUserInfo(String customerId);
    
    boolean updatePassword(String customerId, String password);
    
    boolean updateUserInfo(Customer customer);
    
    boolean insertUserWithValidation(Customer customer);
    
    boolean deleteCustomer(Integer id);
    
    boolean updateUserWithValidation(Customer customer);
    
}
