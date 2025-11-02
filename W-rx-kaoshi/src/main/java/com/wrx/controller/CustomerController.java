package com.wrx.controller;


import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
import com.wrx.entity.Employee;
import com.wrx.entity.SysUser;
import com.wrx.service.ICustomerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    //注册
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Customer customer){
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = customerService.register(customer);
            if (success) {
                result.put("success", true);
                result.put("message", "注册成功");
            } else {
                result.put("success", false);
                result.put("message", "注册失败，账号或手机号可能已存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "注册过程中出现错误：" + e.getMessage());
        }
        
        return result;
    }
    @GetMapping("/allUser")
    public Map<String,Object> selectAllUser(Customer customer, int pageIndex, int pageSize){
        return customerService.selectAllUser(customer,pageIndex,pageSize);
    }
    //根据账号或用户名搜索
    @PostMapping("/selectCustomerByIdOrName")
    public Map<String, Object> selectCustomerByIdOrName(@RequestBody Customer customer){
        try {
            return customerService.selectCustomerByIdOrName(customer);
        } catch (Exception e) {
            // 可以添加日志记录
            Map<String, Object> result = new HashMap<>();
            result.put("records", Collections.emptyList());
            return result;
        }
    }

}
