package com.wrx.controller;


import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Employee;
import com.wrx.entity.SysUser;
import com.wrx.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@RestController
@CrossOrigin
@RequestMapping("/sysuser")
public class SysUserController {
    @Resource
    private ISysUserService sysUserService;
    //登录
    @PostMapping("/login")
    public LoginUserDto login(@RequestBody SysUser sysUser){
        return sysUserService.login(sysUser);
    }

}
