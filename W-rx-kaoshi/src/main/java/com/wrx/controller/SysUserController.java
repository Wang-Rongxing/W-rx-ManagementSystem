package com.wrx.controller;


import com.wrx.common.Result;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.SysUser;
import com.wrx.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/sysuser")
public class SysUserController {
    @Resource
    private ISysUserService sysUserService;

    @PostMapping("/login")
    public Result<LoginUserDto> login(@RequestBody SysUser sysUser){
        LoginUserDto loginUserDto = sysUserService.login(sysUser);
        return Result.success("登录成功", loginUserDto);
    }

}
