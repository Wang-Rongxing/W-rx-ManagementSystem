package com.wrx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.entity.Customer;
import com.wrx.mapper.CustomerMapper;
import com.wrx.service.ICustomerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
