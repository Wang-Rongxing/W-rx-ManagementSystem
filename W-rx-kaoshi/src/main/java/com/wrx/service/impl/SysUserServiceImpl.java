package com.wrx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.entity.SysUser;
import com.wrx.mapper.SysUserMapper;
import com.wrx.service.ISysUserService;
import org.springframework.stereotype.Service;

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

}
