package com.baomidou.mybatisplus.service.impl;

import com.baomidou.mybatisplus.entity.SysJobLog;
import com.baomidou.mybatisplus.mapper.SysJobLogMapper;
import com.baomidou.mybatisplus.service.ISysJobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2026-03-26
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements ISysJobLogService {

}
