package com.wrx.controller;


import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
import com.wrx.entity.SysUser;
import com.wrx.service.ICustomerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@RestController
@CrossOrigin
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private ICustomerService customerService;
    //登录
    @PostMapping("/login")
    public LoginUserDto login(@RequestBody Customer customer){
        return customerService.login(customer);
    }

}
