package com.wrx.controller;

import com.wrx.entity.Customer;
import com.wrx.entity.Orders;
import com.wrx.service.IOrdersService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private IOrdersService ordersService;

    /**
     * 查询订单列表
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 订单列表及分页信息
     */
    @GetMapping("/allOrders")
    public Map<String, Object> getAllOrders(Orders orders, @RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "10") int pageSize) {
        return ordersService.selectAllUser(orders, pageIndex, pageSize);
    }


    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 取消结果
     */
    @DeleteMapping("/delete/{orderId}")
    public Map<String, Object> cancelOrder(@PathVariable Integer orderId) {
        Map<String, Object> result = new HashMap<>();
        
        if (orderId == null) {
            result.put("code", "400");
            result.put("message", "订单ID不能为空");
            return result;
        }
        
        try {
            // 调用自定义的取消订单方法，该方法会同时更新客房状态
            boolean success = ordersService.cancelOrder(orderId);
            if (success) {
                result.put("code", "200");
                result.put("message", "取消订单成功");
            } else {
                result.put("code", "400");
                result.put("message", "取消订单失败，订单不存在");
            }
        } catch (Exception e) {
            result.put("code", "500");
            result.put("message", "取消订单异常：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 办理入住
     * @param orderId 订单ID
     * @return 办理结果
     */
    @PostMapping("/checkIn/{orderId}")
    public Map<String, Object> checkIn(@PathVariable Integer orderId) {
        Map<String, Object> result = new HashMap<>();
        
        if (orderId == null) {
            result.put("code", "400");
            result.put("message", "订单ID不能为空");
            return result;
        }
        
        try {
            // 调用办理入住方法
            boolean success = ordersService.checkIn(orderId);
            if (success) {
                result.put("code", "200");
                result.put("message", "办理入住成功");
            } else {
                result.put("code", "400");
                result.put("message", "办理入住失败，订单不存在或数据不完整");
            }
        } catch (Exception e) {
            result.put("code", "500");
            result.put("message", "办理入住异常：" + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 根据客户姓名和电话查询订单
     * @param customerId 客户姓名
     * @param page 页码
     * @param pageSize 每页大小
     * @return 订单列表及分页信息
     */
    @GetMapping("/getOrders")
    public Map<String, Object> getOrders(@RequestParam(required = false) String customerId,
                                        @RequestParam(defaultValue = "1") int page, 
                                        @RequestParam(defaultValue = "10") int pageSize) {
        return ordersService.getOrdersByCustomerId(customerId, page, pageSize);
    }

    @GetMapping("/selectbynameandphone")
    public Map<String, Object> selectbynameandphone(@RequestParam(required = false) String customerName, 
                                                   @RequestParam(required = false) String customerPhone, 
                                                   @RequestParam(defaultValue = "1") int pageIndex, 
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return ordersService.selectbynameandphone(customerName, customerPhone, pageIndex, pageSize);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", "500");
            result.put("message", "查询异常：" + e.getMessage());
            result.put("records", new ArrayList<>());
            result.put("total", 0);
            return result;
        }
    }
    
    /**
     * 添加订单接口
     * @param params 包含订单信息的参数
     * @return 是否添加成功
     */
    @PostMapping("/add")
    public boolean addOrder(@RequestBody Map<String, Object> params) {
        String customerId = (String) params.get("customerId");
        String roomNumber = (String) params.get("roomNumber");
        
        // 解析日期参数
        String checkInDateStr = (String) params.get("checkInDate");
        String checkOutDateStr = (String) params.get("checkOutDate");
        
        LocalDate checkInDate = LocalDate.parse(checkInDateStr);
        LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);
        
        return ordersService.addOrder(customerId, roomNumber, checkInDate, checkOutDate);
    }
}
