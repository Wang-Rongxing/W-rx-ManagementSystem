package com.wrx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrx.dto.CheckInDto;
import com.wrx.dto.OrderDto;
import com.wrx.entity.CheckIn;
import com.wrx.entity.Customer;
import com.wrx.entity.Orders;
import com.wrx.entity.Room;
import com.wrx.mapper.CustomerMapper;
import com.wrx.mapper.OrdersMapper;
import com.wrx.mapper.RoomMapper;
import com.wrx.service.IOrdersService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private IOrdersService orderService;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private OrdersMapper ordersMapper;

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
            boolean success = orderService.checkIn(orderId);
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
        Map<String, Object> result = new HashMap<>();
        try {
            // 根据customerId查询客户信息，获取数据库主键ID
            QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
            customerQueryWrapper.eq("customer_id", customerId);
            Customer customer = customerMapper.selectOne(customerQueryWrapper);

            if (customer == null) {
                result.put("success", false);
                result.put("message", "客户不存在");
                return result;
            }

            // 创建查询条件，根据客户的数据库ID查询入住记录
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("customer_id", customer.getId());
            queryWrapper.orderByDesc("check_in_date"); // 按入住时间倒序

            // 创建分页对象
            Page<Orders> pageObj = new Page<>(page, pageSize);

            // 执行分页查询
            Page<Orders> ordersPage = ordersMapper.selectPage(pageObj, queryWrapper);
            List<Orders> ordersList = ordersPage.getRecords();

            // 转换为DTO并关联查询房间信息
            List<OrderDto> orderDtoList = new ArrayList<>();
            for (Orders orders : ordersList) {
                OrderDto orderDto = new OrderDto();

                // 设置基本信息
                orderDto.setOrderId(orders.getOrderId());
                orderDto.setCheckInDate(orders.getCheckInDate());
                orderDto.setCheckOutDate(orders.getCheckOutDate());
                orderDto.setTotalPrice(orders.getAmount());
                orderDto.setCreateTime(orders.getCreateTime());
                orderDto.setCustomerName(customer.getName());
                orderDto.setCustomerPhone(customer.getPhone());

                // 通过roomId查询房间信息
                if (orders.getRoomId() != null) {
                    Room room = roomMapper.selectById(orders.getRoomId());
                    if (room != null) {
                        orderDto.setRoomType(room.getRoomType());
                        orderDto.setRoomNumber(room.getRoomNumber());
                    }
                }

                orderDtoList.add(orderDto);
            }

            // 构造返回结果
            result.put("records", orderDtoList);
            result.put("total", ordersPage.getTotal());
            result.put("success", true);
            result.put("message", "查询成功");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "查询失败：" + e.getMessage());
            result.put("records", new ArrayList<>());
            result.put("total", 0);
        }

        return result;
    }

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
