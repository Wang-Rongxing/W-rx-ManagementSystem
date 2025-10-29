package com.wrx.controller;

import com.wrx.dto.LoginUserDto;
import com.wrx.dto.UserDto;
import com.wrx.entity.User;
import com.wrx.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    /*
    1、分页查询user（所有或根据条件）
    2、根据user_id查询SysUserRole
    3、根据SysUserRole的role_id查询Role
     */
    //@PreAuthorize("hasAnyAuthority('ROLE_sys_admin','ROLE_edu_admin')")
    @PreAuthorize("hasAuthority('ROLE_sys_admin')")
    @GetMapping("/userWithRoleByPage")
    public Map<String,Object> selectUserAndRole(User user,int pageIndex, int pageSize){
        return userService.selectUserAndRole(user,pageIndex,pageSize);
    }
    @PreAuthorize("hasAnyRole('sys_admin','edu_admin')")//自动补全ROLE_
    @PostMapping("/updateUserRole")
    public boolean updateUserRole(@RequestBody UserDto userDto){
        return userService.updateUserRole(userDto);
    }

    @PreAuthorize("hasAuthority('ROLE_sys_admin')")
    @GetMapping("/allUser")
    public Map<String,Object> selectAllUser(User user,int pageIndex, int pageSize){
        return userService.selectAllUser(user,pageIndex,pageSize);
    }
    @PreAuthorize("hasAuthority('ROLE_sys_admin')")
    @PostMapping("/initialPerminssion")
    public boolean updateUserinitialPerminssion(){
        return userService.updateUserinitialPerminssion();
    }
    //登录
    @PostMapping("/login")
    public LoginUserDto login(@RequestBody User user){
        return userService.login(user);
    }
}
