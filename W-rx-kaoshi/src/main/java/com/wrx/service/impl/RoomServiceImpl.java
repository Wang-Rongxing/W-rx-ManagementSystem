package com.wrx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.entity.Room;
import com.wrx.mapper.RoomMapper;
import com.wrx.service.IRoomService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客房表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

}
