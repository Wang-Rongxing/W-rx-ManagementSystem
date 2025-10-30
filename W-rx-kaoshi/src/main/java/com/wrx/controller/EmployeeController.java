package com.wrx.controller;

import com.wrx.dto.LoginUserDto;
import com.wrx.dto.EmployeeDto;
import com.wrx.entity.Employee;
import com.wrx.service.IEmployeeService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    //@PreAuthorize("hasAnyRole('sys_admin','edu_admin')")//自动补全ROLE_
    @PostMapping("/updateUserRole")
    public boolean updateUserRole(@RequestBody EmployeeDto employeeDto){
        return userService.updateUserRole(employeeDto);
    }

    @GetMapping("/allUser")
    public Map<String,Object> selectAllUser(Employee employee, int pageIndex, int pageSize){
        return userService.selectAllUser(employee,pageIndex,pageSize);
    }

    @PostMapping("/initialPerminssion")
    public boolean updateUserinitialPerminssion(){
        return userService.updateUserinitialPerminssion();
    }
    //登录
    @PostMapping("/login")
    public LoginUserDto login(@RequestBody Employee employee){
        return userService.login(employee);
    }
    
    //删除员工
    @DeleteMapping("/{id}")
    public boolean deleteEmployee(@PathVariable Integer id){
        if (id == null || id <= 0) {
            return false;
        }
        try {
            return userService.removeById(id);
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
}
