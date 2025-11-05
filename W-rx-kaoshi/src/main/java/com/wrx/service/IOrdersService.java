package com.wrx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.entity.Orders;

import java.util.Map;
import java.time.LocalDate;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
public interface IOrdersService extends IService<Orders> {

    /**
     * 分页查询所有订单并转换为OrderDto
     * @param orders 查询条件
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 包含订单列表和总数的Map
     */
    Map<String, Object> selectAllUser(Orders orders, int pageIndex, int pageSize);

    /**
     * 根据客户姓名和电话查询订单
     * @param customerName 客户姓名
     * @param customerPhone 客户电话
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 包含订单列表和总数的Map
     */
    Map<String, Object> selectbynameandphone(String customerName, String customerPhone, int pageIndex, int pageSize);
    
    /**
     * 取消订单并更新客房状态
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean cancelOrder(Integer orderId);
    
    /**
     * 办理入住
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean checkIn(Integer orderId);
    
    /**
     * 添加订单
     * @param customerId 客户ID
     * @param roomNumber 房间号
     * @param checkInDate 入住日期
     * @param checkOutDate 退房日期
     * @return 是否成功
     */
    boolean addOrder(String customerId, String roomNumber, LocalDate checkInDate, LocalDate checkOutDate);

}
