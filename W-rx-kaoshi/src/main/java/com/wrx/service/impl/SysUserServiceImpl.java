package com.wrx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.SysUser;
import com.wrx.mapper.SysUserMapper;
import com.wrx.service.ISysUserService;
import com.wrx.util.JwtUtil;
import com.wrx.util.LoginSysUSer;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Override
    public LoginUserDto login(SysUser sysUser) {
        String usernameWithRole = "admin:" + sysUser.getAccount();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameWithRole, sysUser.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        LoginSysUSer loginSysUSer = (LoginSysUSer) authentication.getPrincipal();
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setName(loginSysUSer.getSysUser().getName());
        loginUserDto.setEmployeeId(loginSysUSer.getSysUser().getAccount());
        String token = JwtUtil.creatToken(loginSysUSer.getSysUser().getId());
        loginUserDto.setToken(token);
        List<String> roles = new ArrayList<>();
        roles.add(loginSysUSer.getSysUser().getRole());
        loginUserDto.setRoles(roles);
        return loginUserDto;
    }
}
