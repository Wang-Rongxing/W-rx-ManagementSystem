package com.wrx.controller;


import com.wrx.common.Result;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
import com.wrx.service.ICustomerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private ICustomerService customerService;

    @PostMapping("/login")
    public Result<LoginUserDto> login(@RequestBody Customer customer){
        LoginUserDto loginUserDto = customerService.login(customer);
        return Result.success("登录成功", loginUserDto);
    }

    @GetMapping("/getUserInfo")
    public Result<Customer> getUserInfo(@RequestParam String customerId) {
        Customer customer = customerService.getUserInfo(customerId);
        return Result.success(customer);
    }

    @PostMapping("/updatePassword")
    public Result<Boolean> updatePassword(@RequestBody Map<String, String> passwordData) {
        String customerId = passwordData.get("customerId");
        String password = passwordData.get("password");
        boolean success = customerService.updatePassword(customerId, password);
        return success ? Result.success("密码修改成功", success) : Result.error("密码修改失败");
    }
    
    @PostMapping("/updateUserInfo")
    public Result<Boolean> updateUserInfo(@RequestBody Customer customer) {
        boolean success = customerService.updateUserInfo(customer);
        return success ? Result.success("更新成功", success) : Result.error("更新失败");
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody Customer customer) {
        boolean success = customerService.register(customer);
        return success ? Result.success("注册成功", success) : Result.error("注册失败，账号或手机号可能已存在");
    }
    
    @GetMapping("/allUser")
    public Result<Map<String, Object>> selectAllUser(Customer customer, int pageIndex, int pageSize) {
        Map<String, Object> data = customerService.selectAllUser(customer, pageIndex, pageSize);
        return Result.success(data);
    }
    
    @PostMapping("/selectCustomerByIdOrName")
    public Result<Map<String, Object>> selectCustomerByIdOrName(@RequestBody Customer customer) {
        Map<String, Object> data = customerService.selectCustomerByIdOrName(customer);
        return Result.success(data);
    }
    
    @PostMapping("/insertUser")
    public Result<Boolean> insertUser(@RequestBody Customer customer) {
        boolean result = customerService.insertUserWithValidation(customer);
        return result ? Result.success("添加成功", result) : Result.error("添加失败");
    }
    
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteCustomer(@PathVariable Integer id) {  
        boolean success = customerService.deleteCustomer(id);
        return success ? Result.success("删除成功", success) : Result.error("删除失败");
    }
    
    @PostMapping("/updateUser")
    public Result<Boolean> updateUser(@RequestBody Customer customer) {
        boolean result = customerService.updateUserWithValidation(customer);
        return result ? Result.success("更新成功", result) : Result.error("更新失败");
    }
    
    @GetMapping("/getHistory")
    public Result<Map<String, Object>> getHistory(@RequestParam String customerId,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = customerService.getHistory(customerId, page, pageSize);
        return Result.success(data);
    }

}
