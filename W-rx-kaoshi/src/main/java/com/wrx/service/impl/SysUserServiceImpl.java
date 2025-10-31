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
    @Resource
    private PasswordEncoder passwordEncoder;
    @Override
    public LoginUserDto login(SysUser sysUser) {
        //判断获取的 UserDetails 信息中的密码是否和前端提交的密码一致，
        //security调用AuthenticationManager进行认证
        // 如果一致返回一个通过验证了的 UserDetails（LoginUser），如果密码不一致抛出异常
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(sysUser.getAccount(), sysUser.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //把合法的authentication的内容处理或封装到LoginUserDto
        LoginSysUSer loginSysUSer = (LoginSysUSer) authentication.getPrincipal();
        //密码验证成功，根据 user 的 id 查询对应的 SysRole 信息并存入 loginUser

        //TODO 把user及角色信息存入redis
        LoginUserDto loginUserDto = new LoginUserDto();
        //复制Employee，粘贴到LoginEmployeeDto
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
