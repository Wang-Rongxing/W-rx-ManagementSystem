package com.wrx.controller;

import com.wrx.entity.Room;
import com.wrx.service.IRoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客房表 前端控制器
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@RestController
@CrossOrigin
@RequestMapping("/room")
public class RoomController {
    @Resource
    private IRoomService roomService;

    // 查询所有客房信息
    @GetMapping("/allRooms")
    public Map<String, Object> selectAllRooms(Room room, Integer pageIndex, Integer pageSize) {
        return roomService.selectAllRooms(room, pageIndex, pageSize);
    }

    // 根据条件搜索客房
    @PostMapping("/selectRoomByCondition")
    public Map<String, Object> selectRoomByCondition(@RequestBody Room room) {
        return roomService.selectRoomByCondition(room);
    }

    // 新增客房
    @PostMapping("/insertRoom")
    public boolean insertRoom(@RequestBody Room room) {
        return roomService.insertRoom(room);
    }

    // 更新客房信息
    @PostMapping("/updateRoom")
    public boolean updateRoom(@RequestBody Room room) {
        return roomService.updateRoom(room);
    }

    // 删除客房
    @DeleteMapping("/delete/{id}")
    public boolean deleteRoom(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return false;
        }
        try {
            return roomService.removeById(id);
        } catch (Exception e) {
            System.out.println("删除客房异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    // 获取所有客房类型
    @GetMapping("/types")
    public List<String> getRoomTypes() {
        return roomService.getRoomTypes();
    }
    
    // 根据客房类型和状态查询客房
    @GetMapping("/selectRoomByroomTypeAndStatus")
    public List<String> selectRoomByroomTypeAndStatus(@RequestParam(required = false) String roomType,
                                                  @RequestParam(required = false) Integer status) {
        return roomService.selectRoomByroomTypeAndStatus(roomType, status);
    }
}
