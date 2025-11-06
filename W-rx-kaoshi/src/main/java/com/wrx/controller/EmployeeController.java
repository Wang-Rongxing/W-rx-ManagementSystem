package com.wrx.controller;

import com.wrx.dto.LoginUserDto;
import com.wrx.dto.EmployeeDto;
import com.wrx.entity.Employee;
import com.wrx.service.IEmployeeService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private IEmployeeService userService;
    /*
    1、分页查询user（所有或根据条件）
    2、根据user_id查询SysUserRole
    3、根据SysUserRole的role_id查询Role
     */
    //@PreAuthorize("hasAnyAuthority('ROLE_sys_admin','ROLE_edu_admin')")
    //@PreAuthorize("hasAuthority('ROLE_sys_admin')")
    @GetMapping("/userWithRoleByPage")
    public Map<String,Object> selectUserAndRole(Employee employee, int pageIndex, int pageSize){
        return userService.selectUserAndRole(employee,pageIndex,pageSize);
    }
    
    //新增员工
    @PostMapping("/insertUser")
    public boolean insertUser(@RequestBody Employee employee){
        if (employee == null || employee.getEmployeeId() == null || employee.getEmployeeId().isEmpty()
                || employee.getName() == null || employee.getName().isEmpty()
                || employee.getPassword() == null || employee.getPassword().isEmpty()) {
            return false;
        }
        try {
            return userService.insertUser(employee);
        } catch (Exception e) {
            // 可以添加日志记录
            return false;
        }
    }
    //@PreAuthorize("hasAnyRole('sys_admin','edu_admin')")//自动补全ROLE_
    @PostMapping("/updateUserRole")
    public boolean updateUserRole(@RequestBody EmployeeDto employeeDto){
        return userService.updateUserRole(employeeDto);
    }

    @GetMapping("/allUser")
    public Map<String,Object> selectAllUser(Employee employee, int pageIndex, int pageSize){
        return userService.selectAllUser(employee,pageIndex,pageSize);
    }
    //登录
    @PostMapping("/login")
    public LoginUserDto login(@RequestBody Employee employee){
        return userService.login(employee);
    }
    
    //删除员工及其对应的角色关系
    @DeleteMapping("/delete/{id}")
    public boolean deleteEmployee(@PathVariable Integer id){
        if (id == null || id <= 0) {
            return false;
        }
        try {
            // 调用新方法同时删除员工及其对应的EmployeeRole数据
            return userService.deleteEmployeeAndRole(id);
        } catch (Exception e) {
            // 可以添加日志记录
            return false;
        }
    }
    
    //重置密码
    @GetMapping("/resetUserPassword")
    public boolean resetUserPassword(Integer id, String employeeId){
        if (id == null || id <= 0 || employeeId == null || employeeId.isEmpty()) {
            return false;
        }
        try {
            return userService.resetUserPassword(id, employeeId);
        } catch (Exception e) {
            // 可以添加日志记录
            return false;
        }
    }
    
    //根据工号或用户名搜索
    @PostMapping("/selectEmployeeByIdOrName")
    public Map<String, Object> selectEmployeeByIdOrName(@RequestBody Employee employee){
        try {
            return userService.selectEmployeeByIdOrName(employee);
        } catch (Exception e) {
            // 可以添加日志记录
            Map<String, Object> result = new HashMap<>();
            result.put("records", Collections.emptyList());
            return result;
        }
    }
}
