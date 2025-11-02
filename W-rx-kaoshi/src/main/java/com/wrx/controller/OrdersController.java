package com.wrx.controller;

import com.wrx.entity.Orders;
import com.wrx.service.IOrdersService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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
    private IOrdersService orderService;

    /**
     * 查询订单列表
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 订单列表及分页信息
     */
    @GetMapping("/allOrders")
    public Map<String, Object> getAllOrders(Orders orders, @RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "10") int pageSize) {
        return orderService.selectAllUser(orders, pageIndex, pageSize);
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
            boolean success = orderService.cancelOrder(orderId);
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
     * 根据客户姓名和电话查询订单
     * @param customerName 客户姓名
     * @param customerPhone 客户电话
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 订单列表及分页信息
     */
    @GetMapping("/selectbynameandphone")
    public Map<String, Object> selectbynameandphone(@RequestParam(required = false) String customerName, 
                                                   @RequestParam(required = false) String customerPhone, 
                                                   @RequestParam(defaultValue = "1") int pageIndex, 
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        try {
            return orderService.selectbynameandphone(customerName, customerPhone, pageIndex, pageSize);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", "500");
            result.put("message", "查询异常：" + e.getMessage());
            result.put("records", new ArrayList<>());
            result.put("total", 0);
            return result;
        }
    }
}
