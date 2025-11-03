package com.wrx.controller;


import com.wrx.entity.CheckIn;
import com.wrx.service.ICheckInService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 入住登记表 前端控制器
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Controller
@CrossOrigin
@RequestMapping("/checkin")
public class CheckInController {
    @Resource
    private ICheckInService checkInService;
    
    /**
     * 查询所有入住登记信息
     * @param checkIn 查询条件
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 入住登记列表
     */
    @GetMapping("/allCheckin")
    @ResponseBody
    public Map<String, Object> getAllCheckin(CheckIn checkIn, 
                                           @RequestParam(defaultValue = "1") int pageIndex, 
                                           @RequestParam(defaultValue = "10") int pageSize) {
        return checkInService.selectAllUser(checkIn, pageIndex, pageSize);
    }
    
    /**
     * 办理退房手续
     * @param request 包含checkInId的请求对象
     * @return 退房结果
     */
    @PostMapping("/checkOut")
    @ResponseBody
    public Map<String, Object> checkOut(@RequestBody Map<String, Integer> request) {
        Integer checkInId = request.get("checkInId");
        if (checkInId == null) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("code", "400");
            errorResult.put("message", "缺少必要参数checkInId");
            return errorResult;
        }
        return checkInService.checkOut(checkInId);
    }
    
    /**
     * 根据客户姓名和电话查询入住记录
     * @param customerName 客户姓名
     * @param customerPhone 客户电话
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 入住记录列表
     */
    @GetMapping("/selectbynameandphone")
    @ResponseBody
    public Map<String, Object> selectbynameandphone(@RequestParam(required = false) String customerName,
                                                 @RequestParam(required = false) String customerPhone,
                                                 @RequestParam(defaultValue = "1") int pageIndex,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return checkInService.selectbynameandphone(customerName, customerPhone, pageIndex, pageSize);
    }
    
    /**
     * 直接添加入住记录
     * @param request 包含入住信息的请求对象
     * @return 操作结果
     */
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addCheckIn(@RequestBody Map<String, String> request) {
        String customerName = request.get("customerName");
        String customerPhone = request.get("customerPhone");
        String roomType = request.get("roomType");
        String roomNumber = request.get("roomNumber");
        
        return checkInService.addCheckIn(customerName, customerPhone, roomType, roomNumber);
    }
}
