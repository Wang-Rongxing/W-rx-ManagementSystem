package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.entity.Room;
import com.wrx.mapper.RoomMapper;
import com.wrx.service.IRoomService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 查询所有客房信息（分页）
     * @param room 查询条件
     * @param pageIndex 页码
     * @param pageSize 每页条数
     * @return 查询结果
     */
    @Override
    public Map<String, Object> selectAllRooms(Room room, Integer pageIndex, Integer pageSize) {
        // 设置默认值，避免空指针异常
        if (pageIndex == null || pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        try {
            Map<String, Object> result = new HashMap<>();
            // 使用MyBatis-Plus的分页查询
            Page<Room> page = new Page<>(pageIndex, pageSize);
            // 构建查询条件
            LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
            if (room.getRoomNumber() != null && !room.getRoomNumber().isEmpty()) {
                wrapper.eq(Room::getRoomNumber, room.getRoomNumber());
            }
            if (room.getRoomType() != null && !room.getRoomType().isEmpty()) {
                wrapper.eq(Room::getRoomType, room.getRoomType());
            }
            if (room.getStatus() != null) {
                wrapper.eq(Room::getStatus, room.getStatus());
            }
            // 执行分页查询
            Page<Room> roomPage = this.page(page, wrapper);
            result.put("records", roomPage.getRecords());
            result.put("total", roomPage.getTotal());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("records", Collections.emptyList());
            result.put("total", 0);
            result.put("message", "查询客房信息失败：" + e.getMessage());
            return result;
        }
    }

    // 根据条件搜索客房
    @Override
    public Map<String, Object> selectRoomByCondition(Room room) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
            if (room.getRoomNumber() != null && !room.getRoomNumber().isEmpty()) {
                wrapper.eq(Room::getRoomNumber, room.getRoomNumber());
            }
            if (room.getRoomType() != null && !room.getRoomType().isEmpty()) {
                wrapper.eq(Room::getRoomType, room.getRoomType());
            }
            if (room.getStatus() != null) {
                wrapper.eq(Room::getStatus, room.getStatus());
            }
            if (room.getRoomId() != null) {
                wrapper.eq(Room::getRoomId, room.getRoomId());
            }
            // 执行查询
            List<Room> roomList = this.list(wrapper);
            result.put("records", roomList);
            result.put("total", roomList.size());
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("records", Collections.emptyList());
            result.put("total", 0);
            result.put("message", "搜索客房信息失败：" + e.getMessage());
            return result;
        }
    }

    // 新增客房
    @Override
    public boolean insertRoom(Room room) {
        System.out.println("接收到添加客房请求: " + room);
        
        if (room == null) {
            System.out.println("客房对象为空");
            return false;
        }
        
        if (room.getRoomNumber() == null || room.getRoomNumber().isEmpty()) {
            System.out.println("客房编号为空");
            return false;
        }
        
        if (room.getRoomType() == null || room.getRoomType().isEmpty()) {
            System.out.println("客房类型为空");
            return false;
        }
        
        if (room.getPrice() == null) {
            System.out.println("客房价格为空");
            return false;
        }
        
        try {
            // 检查客房编号是否已存在
            LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Room::getRoomNumber, room.getRoomNumber());
            Room existingRoom = this.getOne(wrapper);
            if (existingRoom != null) {
                System.out.println("客房编号已存在: " + room.getRoomNumber());
                return false;
            }
            
            // 设置默认状态为空闲
            if (room.getStatus() == null) {
                room.setStatus(1);
            }
            
            boolean result = this.save(room);
            System.out.println("添加客房结果: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("添加客房异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 更新客房信息
    @Override
    public boolean updateRoom(Room room) {
        System.out.println("接收到更新客房请求: " + room);
        
        if (room == null || room.getRoomId() == null) {
            System.out.println("客房ID为空");
            return false;
        }
        
        try {
            // 检查客房是否存在
            Room existingRoom = this.getById(room.getRoomId());
            if (existingRoom == null) {
                System.out.println("客房不存在");
                return false;
            }
            
            // 如果修改了客房编号，检查新编号是否已存在
            if (room.getRoomNumber() != null && !room.getRoomNumber().equals(existingRoom.getRoomNumber())) {
                LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Room::getRoomNumber, room.getRoomNumber());
                Room checkRoom = this.getOne(wrapper);
                if (checkRoom != null && !checkRoom.getRoomId().equals(room.getRoomId())) {
                    System.out.println("客房编号已存在: " + room.getRoomNumber());
                    return false;
                }
            }
            
            boolean result = this.updateById(room);
            System.out.println("更新客房结果: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("更新客房异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> getRoomTypes() {
        try {
            // 查询所有客房类型，去重
            LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
            wrapper.select(Room::getRoomType);
            wrapper.groupBy(Room::getRoomType);

            List<Room> rooms = this.list(wrapper);
            return rooms.stream()
                    .map(Room::getRoomType)
                    .filter(Objects::nonNull)
                    .filter(type -> !type.isEmpty())
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("获取客房类型失败: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> selectRoomByroomTypeAndStatus(String roomType, Integer status) {
        try {
            LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
            
            // 根据客房类型过滤
            if (roomType != null && !roomType.isEmpty()) {
                wrapper.eq(Room::getRoomType, roomType);
            }
            
            // 根据状态过滤
            if (status != null) {
                wrapper.eq(Room::getStatus, status);
            }
            
            // 查询并返回客房编号列表
            List<Room> rooms = this.list(wrapper);
            List<String> roomNumbers = rooms.stream()
                    .map(Room::getRoomNumber)
                    .filter(Objects::nonNull)
                    .filter(num -> !num.isEmpty())
                    .collect(Collectors.toList());
            
            System.out.println("查询到的客房数量: " + roomNumbers.size() + "，类型: " + roomType + "，状态: " + status);
            return roomNumbers;
        } catch (Exception e) {
            System.out.println("查询客房失败: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
