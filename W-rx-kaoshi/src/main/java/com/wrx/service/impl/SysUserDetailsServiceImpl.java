//package com.wrx.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.wrx.entity.SysUser;
//import com.wrx.mapper.SysUserMapper;
//import com.wrx.util.LoginSysUSer;
//import jakarta.annotation.Resource;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
///*
//*加载需要认证user
// */
//@Service
//public class SysUserDetailsServiceImpl implements UserDetailsService {
//    @Resource
//    private SysUserMapper sysUserMapper;
//    //用登录名查询user，封装到UserDetails（LoginUser）
//    @Override
//    public UserDetails loadUserByUsername(String jobId) throws UsernameNotFoundException {
//        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(SysUser::getAccount,jobId);
//        SysUser sysUser = sysUserMapper.selectOne(wrapper);
//        if (Objects.isNull(sysUser)) {
//            throw new RuntimeException("工号错误或不存在");
//        }
//        LoginSysUSer loginSysUSer = new LoginSysUSer();
//        loginSysUSer.setSysUser(sysUser);
//        return loginSysUSer;
//    }
//}
