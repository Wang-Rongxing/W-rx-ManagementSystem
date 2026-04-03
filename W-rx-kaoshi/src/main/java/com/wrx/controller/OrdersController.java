package com.wrx.controller;

import com.wrx.common.Result;
import com.wrx.entity.Orders;
import com.wrx.service.IOrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@Tag(name = "订单模块",description = "订单增删改查接口")
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private IOrdersService ordersService;

    @Operation(summary = "查询所以订单",description = ("查询所有订单"))
    @GetMapping("/allOrders")
    public Result<Map<String, Object>> getAllOrders(Orders orders, @RequestParam(defaultValue = "1") int pageIndex, @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = ordersService.selectAllUser(orders, pageIndex, pageSize);
        return Result.success(data);
    }

    @DeleteMapping("/delete/{orderId}")
    public Result<Boolean> cancelOrder(@PathVariable Integer orderId) {
        boolean success = ordersService.cancelOrderWithValidation(orderId);
        return success ? Result.success("取消订单成功", success) : Result.error(400, "取消订单失败，订单不存在");
    }
    
    @PostMapping("/checkIn/{orderId}")
    public Result<Boolean> checkIn(@PathVariable Integer orderId) {
        boolean success = ordersService.checkInWithValidation(orderId);
        return success ? Result.success("办理入住成功", success) : Result.error(400, "办理入住失败，订单不存在或数据不完整");
    }
    
    @GetMapping("/getOrders")
    public Result<Map<String, Object>> getOrders(@RequestParam(required = false) String customerId,
                                        @RequestParam(defaultValue = "1") int page, 
                                        @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = ordersService.getOrdersByCustomerId(customerId, page, pageSize);
        return Result.success(data);
    }

    @GetMapping("/selectbynameandphone")
    public Result<Map<String, Object>> selectbynameandphone(@RequestParam(required = false) String customerName, 
                                                   @RequestParam(required = false) String customerPhone, 
                                                   @RequestParam(defaultValue = "1") int pageIndex, 
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> data = ordersService.selectbynameandphone(customerName, customerPhone, pageIndex, pageSize);
        return Result.success(data);
    }
    
    @PostMapping("/add")
    public Result<Boolean> addOrder(@RequestBody Map<String, Object> params) {
        boolean success = ordersService.addOrderWithParams(params);
        return success ? Result.success("添加订单成功", success) : Result.error("添加订单失败");
    }
}
