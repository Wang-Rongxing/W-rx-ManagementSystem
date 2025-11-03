package com.wrx.controller;


import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
import com.wrx.entity.Employee;
import com.wrx.entity.SysUser;
import com.wrx.service.ICustomerService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Resource
    private PasswordEncoder passwordEncoder;
    //登录
    @PostMapping("/login")
    public LoginUserDto login(@RequestBody Customer customer){
        return customerService.login(customer);
    }

    //根据customerId获取用户信息
    @GetMapping("/getUserInfo")
    public Map<String, Object> getUserInfo(@RequestParam String customerId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用服务层方法获取用户信息
            Customer customer = customerService.getCustomerById(customerId);
            
            if (customer != null) {
                result.put("success", true);
                result.put("data", customer);
            } else {
                result.put("success", false);
                result.put("message", "用户不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户信息失败: " + e.getMessage());
        }
        return result;
    }

    //修改密码
    @PostMapping("/updatePassword")
    public Map<String, Object> updatePassword(@RequestBody Map<String, String> passwordData) {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("接收到修改密码请求: " + passwordData);
            
            String customerId = passwordData.get("customerId");
            String password = passwordData.get("password");
            
            // 验证参数
            if (customerId == null || customerId.isEmpty() || password == null || password.isEmpty()) {
                result.put("success", false);
                result.put("message", "参数不完整");
                return result;
            }
            
            // 查询用户信息
            Customer customer = customerService.getCustomerById(customerId);
            if (customer == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }
            
            // 设置新密码
            customer.setPassword(passwordEncoder.encode(password));
            
            // 更新密码
            boolean success = customerService.updateById(customer);
            if (success) {
                result.put("success", true);
                result.put("message", "密码修改成功");
            } else {
                result.put("success", false);
                result.put("message", "密码修改失败");
            }
        } catch (Exception e) {
            System.out.println("修改密码异常: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "修改密码过程中出现错误: " + e.getMessage());
        }
        return result;
    }
    
    //更新用户信息
    @PostMapping("/updateUserInfo")
    public Map<String, Object> updateUserInfo(@RequestBody Customer customer) {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("接收到更新用户信息请求: " + customer);
            
            // 验证客户对象不为空
            if (customer == null || customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
                System.out.println("客户ID为空");
                result.put("success", false);
                result.put("message", "客户ID不能为空");
                return result;
            }
            
            // 根据customerId查找客户
            Customer existingCustomer = customerService.getCustomerById(customer.getCustomerId());
            if (existingCustomer == null) {
                System.out.println("客户不存在");
                result.put("success", false);
                result.put("message", "客户不存在");
                return result;
            }
            
            // 设置id（数据库主键）
            customer.setId(existingCustomer.getId());
            
            // 保留原密码
            customer.setPassword(existingCustomer.getPassword());
            
            // 执行更新
            boolean success = customerService.updateById(customer);
            if (success) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
        } catch (Exception e) {
            System.out.println("更新用户信息异常: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "更新过程中出现错误: " + e.getMessage());
        }
        return result;
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
    
    //插入新客户
    @PostMapping("/insertUser")
    public boolean insertUser(@RequestBody Customer customer){
        System.out.println("接收到添加客户请求: " + customer);
        
        if (customer == null) {
            System.out.println("客户对象为空");
            return false;
        }
        
        if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
            System.out.println("客户ID为空");
            return false;
        }
        
        if (customer.getName() == null || customer.getName().isEmpty()) {
            System.out.println("客户姓名为空");
            return false;
        }
        
        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            System.out.println("客户密码为空");
            return false;
        }
        
        try {
            boolean result = customerService.insertUser(customer);
            System.out.println("添加客户结果: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("添加客户异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    //删除客户
    @DeleteMapping("/delete/{id}")
    public boolean deleteCustomer(@PathVariable Integer id){  
        if (id == null || id <= 0) {
            return false;
        }
        try {
            return customerService.removeById(id);
        } catch (Exception e) {
            // 可以添加日志记录
            System.out.println("删除客户异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    //更新客户信息
    @PostMapping("/updateUser")
    public boolean updateUser(@RequestBody Customer customer){
        System.out.println("接收到更新客户请求: " + customer);
        
        if (customer == null || customer.getId() == null) {
            System.out.println("客户ID为空");
            return false;
        }
        
        try {
            // 检查客户是否存在
            Customer existingCustomer = customerService.getById(customer.getId());
            if (existingCustomer == null) {
                System.out.println("客户不存在");
                return false;
            }
            
            // 如果密码不为空，则进行加密
            if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
                // 避免重复加密
                if (!customer.getPassword().equals(existingCustomer.getPassword())) {
                    customer.setPassword(passwordEncoder.encode(customer.getPassword()));
                }
            } else {
                // 如果密码为空，保留原密码
                customer.setPassword(existingCustomer.getPassword());
            }
            
            boolean result = customerService.updateById(customer);
            System.out.println("更新客户结果: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("更新客户异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
