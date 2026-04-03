package com.baomidou.mybatisplus.service.impl;

import com.baomidou.mybatisplus.entity.SysJob;
import com.baomidou.mybatisplus.mapper.SysJobMapper;
import com.baomidou.mybatisplus.service.ISysJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

}
