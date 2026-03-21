package com.wrx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wrx.entity.Orders;

import java.util.Map;
import java.time.LocalDate;

public interface IOrdersService extends IService<Orders> {

    Map<String, Object> selectAllUser(Orders orders, int pageIndex, int pageSize);

    Map<String, Object> selectbynameandphone(String customerName, String customerPhone, int pageIndex, int pageSize);
    
    boolean cancelOrder(Integer orderId);
    
    boolean checkIn(Integer orderId);
    
    Map<String, Object> getOrdersByCustomerId(String customerId, int page, int pageSize);
    
    boolean addOrder(String customerId, String roomNumber, LocalDate checkInDate, LocalDate checkOutDate);
    
    boolean cancelOrderWithValidation(Integer orderId);
    
    boolean checkInWithValidation(Integer orderId);
    
    boolean addOrderWithParams(Map<String, Object> params);

}
