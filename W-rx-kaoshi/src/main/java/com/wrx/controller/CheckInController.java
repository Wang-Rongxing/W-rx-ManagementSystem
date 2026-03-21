package com.wrx.controller;


import com.wrx.common.Result;
import com.wrx.entity.CheckIn;
import com.wrx.service.ICheckInService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/checkin")
public class CheckInController {
    @Resource
    private ICheckInService checkInService;
    
    @GetMapping("/allCheckin")
    public Result<Map<String, Object>> getAllCheckin(CheckIn checkIn, 
                                           @RequestParam(defaultValue = "1") int pageIndex, 
                                           @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = checkInService.selectAllUser(checkIn, pageIndex, pageSize);
        return Result.success(data);
    }
    
    @PostMapping("/checkOut")
    public Result<Map<String, Object>> checkOut(@RequestBody Map<String, Integer> request) {
        Integer checkInId = request.get("checkInId");
        if (checkInId == null) {
            return Result.error(400, "缺少必要参数checkInId");
        }
        Map<String, Object> data = checkInService.checkOut(checkInId);
        return Result.success(data);
    }
    
    @GetMapping("/selectbynameandphone")
    public Result<Map<String, Object>> selectbynameandphone(@RequestParam(required = false) String customerName,
                                                 @RequestParam(required = false) String customerPhone,
                                                 @RequestParam(defaultValue = "1") int pageIndex,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = checkInService.selectbynameandphone(customerName, customerPhone, pageIndex, pageSize);
        return Result.success(data);
    }
    
    @PostMapping("/add")
    public Result<Map<String, Object>> addCheckIn(@RequestBody Map<String, String> request) {
        String customerName = request.get("customerName");
        String customerPhone = request.get("customerPhone");
        String roomType = request.get("roomType");
        String roomNumber = request.get("roomNumber");
        
        Map<String, Object> data = checkInService.addCheckIn(customerName, customerPhone, roomType, roomNumber);
        return Result.success(data);
    }
}
