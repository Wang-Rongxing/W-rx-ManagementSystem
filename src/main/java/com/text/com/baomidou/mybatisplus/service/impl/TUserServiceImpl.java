package com.baomidou.mybatisplus.service.impl;

import com.baomidou.mybatisplus.entity.TUser;
import com.baomidou.mybatisplus.mapper.TUserMapper;
import com.baomidou.mybatisplus.service.ITUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

}
