package com.wrx.controller;

import com.wrx.common.Result;
import com.wrx.entity.Room;
import com.wrx.service.IRoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/room")
public class RoomController {
    @Resource
    private IRoomService roomService;

    @GetMapping("/allRooms")
    public Result<Map<String, Object>> selectAllRooms(Room room, Integer pageIndex, Integer pageSize) {
        Map<String, Object> data = roomService.selectAllRooms(room, pageIndex, pageSize);
        return Result.success(data);
    }

    @PostMapping("/selectRoomByCondition")
    public Result<Map<String, Object>> selectRoomByCondition(@RequestBody Room room) {
        Map<String, Object> data = roomService.selectRoomByCondition(room);
        return Result.success(data);
    }

    @PostMapping("/insertRoom")
    public Result<Boolean> insertRoom(@RequestBody Room room) {
        boolean success = roomService.insertRoom(room);
        return success ? Result.success("新增成功", success) : Result.error("新增失败");
    }

    @PostMapping("/updateRoom")
    public Result<Boolean> updateRoom(@RequestBody Room room) {
        boolean success = roomService.updateRoom(room);
        return success ? Result.success("更新成功", success) : Result.error("更新失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteRoom(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return Result.error("无效的ID");
        }
        try {
            boolean success = roomService.removeById(id);
            return success ? Result.success("删除成功", success) : Result.error("删除失败");
        } catch (Exception e) {
            System.out.println("删除客房异常: " + e.getMessage());
            e.printStackTrace();
            return Result.error("删除异常: " + e.getMessage());
        }
    }

    @GetMapping("/types")
    public Result<List<String>> getRoomTypes() {
        List<String> data = roomService.getRoomTypes();
        return Result.success(data);
    }
    
    @GetMapping("/selectRoomByroomTypeAndStatus")
    public Result<List<String>> selectRoomByroomTypeAndStatus(@RequestParam(required = false) String roomType,
                                                  @RequestParam(required = false) Integer status) {
        List<String> data = roomService.selectRoomByroomTypeAndStatus(roomType, status);
        return Result.success(data);
    }
}
