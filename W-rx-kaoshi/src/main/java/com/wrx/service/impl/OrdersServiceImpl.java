package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.dto.OrderDto;
import com.wrx.entity.CheckIn;
import com.wrx.entity.Customer;
import com.wrx.entity.Orders;
import com.wrx.entity.Room;
import com.wrx.mapper.CheckInMapper;
import com.wrx.mapper.CustomerMapper;
import com.wrx.mapper.OrdersMapper;
import com.wrx.mapper.RoomMapper;
import com.wrx.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private CheckInMapper checkInMapper;

    @Override
    public Map<String, Object> selectAllUser(Orders orders, int pageIndex, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        // 创建分页对象
        Page<Orders> page = new Page<>(pageIndex, pageSize);
        // 创建查询条件
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        // 执行分页查询
        Page<Orders> orderPage = this.page(page, queryWrapper);
        // 获取订单列表
        List<Orders> ordersList = orderPage.getRecords();
        // 获取总记录数
        long total = orderPage.getTotal();
        
        // 将Order转换为OrderDto并填充关联信息
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Orders ord : ordersList) {
            OrderDto orderDto = new OrderDto();
            // 设置订单基本信息
            orderDto.setOrderId(ord.getOrderId());
            orderDto.setCheckInDate(ord.getCheckInDate());
            orderDto.setCheckOutDate(ord.getCheckOutDate());
            orderDto.setTotalPrice(ord.getAmount());
            orderDto.setCreateTime(ord.getCreateTime());
            
            // 通过customerId查询客户信息（使用id字段关联）
            Integer customerId = ord.getCustomerId();
            if (customerId != null) {
                QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
                customerQueryWrapper.eq("id", customerId);
                Customer customer = customerMapper.selectOne(customerQueryWrapper);
                if (customer != null) {
                    orderDto.setCustomerName(customer.getName());
                    orderDto.setCustomerPhone(customer.getPhone());
                }
            }
            
            // 通过roomId查询房间信息
            Integer roomId = ord.getRoomId();
            if (roomId != null) {
                QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
                roomQueryWrapper.eq("room_id", roomId);
                Room room = roomMapper.selectOne(roomQueryWrapper);
                if (room != null) {
                    orderDto.setRoomType(room.getRoomType());
                    orderDto.setRoomNumber(room.getRoomNumber());
                }
            }
            
            orderDtoList.add(orderDto);
        }
        
        // 封装结果
        result.put("records", orderDtoList);
        result.put("total", total);
        return result;
    }

    @Override
    public Map<String, Object> selectbynameandphone(String customerName, String customerPhone, int pageIndex, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        // 创建分页对象
        Page<Orders> page = new Page<>(pageIndex, pageSize);
        // 创建查询条件
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        
        // 先查询符合条件的客户ID
        List<Integer> customerIds = new ArrayList<>();
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        if (customerName != null && !customerName.isEmpty()) {
            customerQueryWrapper.like("name", customerName);
        }
        if (customerPhone != null && !customerPhone.isEmpty()) {
            customerQueryWrapper.like("phone", customerPhone);
        }
        
        List<Customer> customers = customerMapper.selectList(customerQueryWrapper);
        for (Customer customer : customers) {
            customerIds.add(customer.getId());
        }
        
        // 如果有符合条件的客户，查询对应的订单
        if (!customerIds.isEmpty()) {
            queryWrapper.in("customer_id", customerIds);
        } else {
            // 如果没有符合条件的客户，返回空结果
            result.put("records", new ArrayList<>());
            result.put("total", 0);
            return result;
        }
        
        // 执行分页查询
        Page<Orders> orderPage = this.page(page, queryWrapper);
        // 获取订单列表
        List<Orders> ordersList = orderPage.getRecords();
        // 获取总记录数
        long total = orderPage.getTotal();
        
        // 将Order转换为OrderDto并填充关联信息
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Orders ord : ordersList) {
            OrderDto orderDto = new OrderDto();
            // 设置订单基本信息
            orderDto.setOrderId(ord.getOrderId());
            orderDto.setCheckInDate(ord.getCheckInDate());
            orderDto.setCheckOutDate(ord.getCheckOutDate());
            orderDto.setTotalPrice(ord.getAmount());
            orderDto.setCreateTime(ord.getCreateTime());
            
            // 通过customerId查询客户信息（使用id字段关联）
            Integer customerId = ord.getCustomerId();
            if (customerId != null) {
                QueryWrapper<Customer> cQueryWrapper = new QueryWrapper<>();
                cQueryWrapper.eq("id", customerId);
                Customer customer = customerMapper.selectOne(cQueryWrapper);
                if (customer != null) {
                    orderDto.setCustomerName(customer.getName());
                    orderDto.setCustomerPhone(customer.getPhone());
                }
            }
            
            // 通过roomId查询房间信息
            Integer roomId = ord.getRoomId();
            if (roomId != null) {
                QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
                roomQueryWrapper.eq("room_id", roomId);
                Room room = roomMapper.selectOne(roomQueryWrapper);
                if (room != null) {
                    orderDto.setRoomNumber(room.getRoomNumber());
                }
            }
            
            orderDtoList.add(orderDto);
        }
        
        // 封装结果
        result.put("records", orderDtoList);
        result.put("total", total);
        return result;
    }
    
    /**
     * 取消订单并更新客房状态
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean cancelOrder(Integer orderId) {
        // 1. 根据订单ID查询订单信息
        Orders order = this.getById(orderId);
        if (order == null) {
            return false;
        }
        
        // 2. 获取订单关联的客房ID
        Integer roomId = order.getRoomId();
        if (roomId != null) {
            // 3. 查询客房信息
            QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
            roomQueryWrapper.eq("room_id", roomId);
            Room room = roomMapper.selectOne(roomQueryWrapper);
            
            if (room != null) {
                // 4. 将客房状态更新为空闲（假设0表示空闲状态）
                room.setStatus(1);
                roomMapper.updateById(room);
            }
        }
        
        // 5. 删除订单
        return this.removeById(orderId);
    }
    
    /**
     * 办理入住
     * 1. 查询订单信息
     * 2. 创建入住记录
     * 3. 更新客房状态为已入住（3）
     * 4. 删除订单
     * @param orderId 订单ID
     * @return 是否成功
     */
    @Override
    @Transactional
    public boolean checkIn(Integer orderId) {
        // 1. 根据订单ID查询订单信息
        Orders order = this.getById(orderId);
        if (order == null) {
            return false;
        }
        
        // 2. 获取订单关联的客户ID和客房ID
        Integer customerId = order.getCustomerId();
        Integer roomId = order.getRoomId();
        
        if (customerId == null || roomId == null) {
            return false;
        }
        
        // 3. 创建入住记录
        CheckIn checkIn = new CheckIn();
        checkIn.setCustomerId(customerId);
        checkIn.setRoomId(roomId);
        checkIn.setActualCheckIn(LocalDateTime.now()); // 入住时间为当前时间
        checkIn.setActualCheckOut(null); // 退房时间为null
        
        boolean saveCheckIn = checkInMapper.insert(checkIn) > 0;
        if (!saveCheckIn) {
            return false;
        }
        
        // 4. 更新客房状态为已入住（3）
        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq("room_id", roomId);
        Room room = roomMapper.selectOne(roomQueryWrapper);
        
        if (room != null) {
            room.setStatus(3); // 3表示已入住
            roomMapper.updateById(room);
        }
        
        // 5. 删除订单
        return this.removeById(orderId);
    }
    
    /**
     * 添加订单
     * 1. 根据customerId查询客户表的id
     * 2. 根据roomNumber查询客房表的id和金额
     * 3. 创建订单记录并存入订单表
     * 4. 更新客房状态为已预订
     */
    @Override
    @Transactional
    public boolean addOrder(String customerId, String roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        // 1. 根据customerId查询客户表的id
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.eq("customer_id", customerId);
        Customer customer = customerMapper.selectOne(customerQueryWrapper);
        if (customer == null) {
            return false;
        }
        Integer dbCustomerId = customer.getId();
        
        // 2. 根据roomNumber查询客房表的id和金额
        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq("room_number", roomNumber);
        Room room = roomMapper.selectOne(roomQueryWrapper);
        if (room == null) {
            return false;
        }
        Integer roomId = room.getRoomId();
        BigDecimal price = room.getPrice();
        
        // 3. 计算订单金额（根据入住天数）
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(days));
        
        // 4. 创建订单记录并存入订单表
        Orders order = new Orders();
        order.setCustomerId(dbCustomerId);
        order.setRoomId(roomId);
        order.setCheckInDate(checkInDate.atStartOfDay());
        order.setCheckOutDate(checkOutDate.atStartOfDay());
        order.setAmount(totalAmount);
        order.setCreateTime(LocalDateTime.now());
        
        boolean saveResult = this.save(order);
        if (!saveResult) {
            return false;
        }
        
        // 5. 更新客房状态为已预订（2表示已预订）
        room.setStatus(2);
        return roomMapper.updateById(room) > 0;
    }

}
