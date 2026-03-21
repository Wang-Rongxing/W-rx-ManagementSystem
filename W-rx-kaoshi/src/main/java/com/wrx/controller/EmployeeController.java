package com.wrx.controller;

import com.wrx.common.Result;
import com.wrx.dto.LoginUserDto;
import com.wrx.dto.EmployeeDto;
import com.wrx.entity.Employee;
import com.wrx.service.IEmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private IEmployeeService userService;
    
    @GetMapping("/userWithRoleByPage")
    public Result<Map<String, Object>> selectUserAndRole(Employee employee, int pageIndex, int pageSize) {
        Map<String, Object> data = userService.selectUserAndRole(employee, pageIndex, pageSize);
        return Result.success(data);
    }
    
    @PostMapping("/insertUser")
    public Result<Boolean> insertUser(@RequestBody Employee employee) {
        if (employee == null || employee.getEmployeeId() == null || employee.getEmployeeId().isEmpty()
                || employee.getName() == null || employee.getName().isEmpty()
                || employee.getPassword() == null || employee.getPassword().isEmpty()) {
            return Result.error("参数不完整");
        }
        try {
            boolean success = userService.insertUser(employee);
            return success ? Result.success("新增成功", success) : Result.error("新增失败，工号已存在");
        } catch (Exception e) {
            return Result.error("新增异常：" + e.getMessage());
        }
    }
    
    @PostMapping("/updateUserRole")
    public Result<Boolean> updateUserRole(@RequestBody EmployeeDto employeeDto) {
        boolean success = userService.updateUserRole(employeeDto);
        return success ? Result.success("更新成功", success) : Result.error("更新失败");
    }

    @GetMapping("/allUser")
    public Result<Map<String, Object>> selectAllUser(Employee employee, int pageIndex, int pageSize) {
        Map<String, Object> data = userService.selectAllUser(employee, pageIndex, pageSize);
        return Result.success(data);
    }
    
    @PostMapping("/login")
    public Result<LoginUserDto> login(@RequestBody Employee employee) {
        LoginUserDto loginUserDto = userService.login(employee);
        return Result.success("登录成功", loginUserDto);
    }
    
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteEmployee(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return Result.error("无效的ID");
        }
        try {
            boolean success = userService.deleteEmployeeAndRole(id);
            return success ? Result.success("删除成功", success) : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error("删除异常：" + e.getMessage());
        }
    }
    
    @GetMapping("/resetUserPassword")
    public Result<Boolean> resetUserPassword(Integer id, String employeeId) {
        if (id == null || id <= 0 || employeeId == null || employeeId.isEmpty()) {
            return Result.error("参数不完整");
        }
        try {
            boolean success = userService.resetUserPassword(id, employeeId);
            return success ? Result.success("重置密码成功", success) : Result.error("重置密码失败");
        } catch (Exception e) {
            return Result.error("重置密码异常：" + e.getMessage());
        }
    }
    
    @PostMapping("/selectEmployeeByIdOrName")
    public Result<Map<String, Object>> selectEmployeeByIdOrName(@RequestBody Employee employee) {
        try {
            Map<String, Object> data = userService.selectEmployeeByIdOrName(employee);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("查询异常：" + e.getMessage());
        }
    }
}
