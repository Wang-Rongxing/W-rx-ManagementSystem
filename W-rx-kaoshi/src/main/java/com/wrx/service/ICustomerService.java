package com.wrx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
import com.wrx.entity.SysUser;

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

}
