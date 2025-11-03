package com.wrx.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
import com.wrx.entity.Employee;
import com.wrx.entity.SysUser;

import java.util.Map;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
public interface ICustomerService extends IService<Customer> {
    //登录
    LoginUserDto login(Customer customer);
    //注册
    boolean register(Customer customer);
    //分页查询user（所有或根据条件）
    Page<Customer> selectByPage(Customer customer, int pageNum, int pageSize);
    //分页查询所有客户
    Map<String, Object> selectAllUser(Customer customer, int pageIndex, int pageSize);

    Map<String, Object> selectCustomerByIdOrName(Customer customer);
    
    //插入新客户
    boolean insertUser(Customer customer);
    
    //根据customerId获取客户信息
    Customer getCustomerById(String customerId);
}
