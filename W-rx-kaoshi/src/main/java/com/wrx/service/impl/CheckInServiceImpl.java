package com.wrx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.entity.CheckIn;
import com.wrx.mapper.CheckInMapper;
import com.wrx.service.ICheckInService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 入住登记表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements ICheckInService {

}
