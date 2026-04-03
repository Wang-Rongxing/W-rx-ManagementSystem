package com.baomidou.mybatisplus.service.impl;

import com.baomidou.mybatisplus.entity.TEmailVerification;
import com.baomidou.mybatisplus.mapper.TEmailVerificationMapper;
import com.baomidou.mybatisplus.service.ITEmailVerificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 邮箱验证码表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Service
public class TEmailVerificationServiceImpl extends ServiceImpl<TEmailVerificationMapper, TEmailVerification> implements ITEmailVerificationService {

}
