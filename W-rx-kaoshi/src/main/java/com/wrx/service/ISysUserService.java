package com.wrx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Employee;
import com.wrx.entity.SysUser;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
public interface ISysUserService extends IService<SysUser> {
    //登录
    LoginUserDto login(SysUser sysUser);

}
