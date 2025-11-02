package com.wrx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.entity.Room;

/**
 * <p>
 * 客房表 服务类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
import com.wrx.entity.Room;

import java.util.Map;

/**
 * <p>
 * 客房表 服务接口
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
public interface IRoomService extends com.baomidou.mybatisplus.extension.service.IService<Room> {

    // 查询所有客房信息（分页）
    Map<String, Object> selectAllRooms(Room room, Integer pageIndex, Integer pageSize);

    // 根据条件搜索客房
    Map<String, Object> selectRoomByCondition(Room room);

    // 新增客房
    boolean insertRoom(Room room);

    // 更新客房信息
    boolean updateRoom(Room room);
}
