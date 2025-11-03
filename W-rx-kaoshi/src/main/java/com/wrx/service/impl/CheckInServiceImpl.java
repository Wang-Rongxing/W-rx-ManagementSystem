package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.dto.CheckInDto;
import com.wrx.entity.CheckIn;
import com.wrx.entity.Customer;
import com.wrx.entity.Room;
import com.wrx.mapper.CheckInMapper;
import com.wrx.mapper.CustomerMapper;
import com.wrx.mapper.RoomMapper;
import com.wrx.service.ICheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 入住登记表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Service
public class CheckInServiceImpl extends ServiceImpl<CheckInMapper, CheckIn> implements ICheckInService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private CheckInMapper checkInMapper;

    /**
     * 查询所有入住登记信息，包含客户和房间信息
     * @param checkIn 查询条件
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 入住登记列表
     */
    @Override
    public Map<String, Object> selectAllUser(CheckIn checkIn, int pageIndex, int pageSize) {
        // 创建查询条件
        QueryWrapper<CheckIn> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (checkIn != null) {
            if (checkIn.getCustomerId() != null) {
                queryWrapper.eq("customer_id", checkIn.getCustomerId());
            }
            if (checkIn.getRoomId() != null) {
                queryWrapper.eq("room_id", checkIn.getRoomId());
            }
            if (checkIn.getActualCheckIn() != null) {
                queryWrapper.ge("actual_check_in", checkIn.getActualCheckIn());
            }
            if (checkIn.getActualCheckOut() != null) {
                queryWrapper.le("actual_check_out", checkIn.getActualCheckOut());
            }
        }
        
        // 创建分页对象
        Page<CheckIn> page = new Page<>(pageIndex, pageSize);
        
        // 查询数据
        Page<CheckIn> checkInPage = baseMapper.selectPage(page, queryWrapper);
        List<CheckIn> checkInList = checkInPage.getRecords();
        
        // 转换为DTO并关联查询客户和房间信息
        List<CheckInDto> checkInDtoList = new ArrayList<>();
        for (CheckIn checkInItem : checkInList) {
            CheckInDto checkInDto = new CheckInDto();
            
            // 设置基本信息
            checkInDto.setCheckInId(checkInItem.getCheckInId());
            checkInDto.setActualCheckIn(checkInItem.getActualCheckIn());
            checkInDto.setActualCheckOut(checkInItem.getActualCheckOut());
            
            // 通过customerId查询客户信息
            if (checkInItem.getCustomerId() != null) {
                Customer customer = customerMapper.selectById(checkInItem.getCustomerId());
                if (customer != null) {
                    checkInDto.setCustomerName(customer.getName());
                    checkInDto.setCustomerPhone(customer.getPhone());
                }
            }
            
            // 通过roomId查询房间信息
            if (checkInItem.getRoomId() != null) {
                Room room = roomMapper.selectById(checkInItem.getRoomId());
                if (room != null) {
                    checkInDto.setRoomType(room.getRoomType());
                    checkInDto.setRoomNumber(room.getRoomNumber());
                }
            }
            
            checkInDtoList.add(checkInDto);
        }
        
        // 构造返回结果，符合前端期望格式
        Map<String, Object> result = new HashMap<>();
        result.put("code", "200");
        result.put("message", "success");
        result.put("records", checkInDtoList);
        result.put("total", checkInPage.getTotal());
        
        return result;
    }
    
    /**
     * 办理退房手续
     * @param checkInId 入住登记ID
     * @return 退房结果
     */
    @Override
    @Transactional
    public Map<String, Object> checkOut(Integer checkInId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 根据ID查询入住记录
            CheckIn checkIn = this.getById(checkInId);
            if (checkIn == null) {
                result.put("code", "400");
                result.put("message", "入住记录不存在");
                return result;
            }
            
            // 2. 检查是否已经退房
            if (checkIn.getActualCheckOut() != null) {
                result.put("code", "400");
                result.put("message", "该客户已经退房");
                return result;
            }
            
            // 3. 更新退房时间
            checkIn.setActualCheckOut(LocalDateTime.now());
            boolean updateResult = this.updateById(checkIn);
            
            if (!updateResult) {
                result.put("code", "500");
                result.put("message", "更新退房信息失败");
                return result;
            }
            
            // 4. 更新房间状态为空闲（假设1表示空闲）
            if (checkIn.getRoomId() != null) {
                Room room = roomMapper.selectById(checkIn.getRoomId());
                if (room != null) {
                    room.setStatus(4); // 设置为空闲状态
                    roomMapper.updateById(room);
                }
            }
            
            result.put("code", "200");
            result.put("message", "退房成功");
        } catch (Exception e) {
            result.put("code", "500");
            result.put("message", "退房过程中出现异常：" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 根据客户姓名和电话查询入住记录
     * @param customerName 客户姓名
     * @param customerPhone 客户电话
     * @param pageIndex 页码
     * @param pageSize 每页大小
     * @return 入住记录列表
     */
    @Override
    public Map<String, Object> selectbynameandphone(String customerName, String customerPhone, int pageIndex, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 先查询符合条件的客户ID
            List<Integer> customerIds = new ArrayList<>();
            
            if ((customerName != null && !customerName.isEmpty()) || (customerPhone != null && !customerPhone.isEmpty())) {
                // 创建客户查询条件
                QueryWrapper<Customer> customerWrapper = new QueryWrapper<>();
                if (customerName != null && !customerName.isEmpty()) {
                    customerWrapper.like("name", customerName);
                }
                if (customerPhone != null && !customerPhone.isEmpty()) {
                    customerWrapper.like("phone", customerPhone);
                }
                
                // 查询符合条件的客户
                List<Customer> customers = customerMapper.selectList(customerWrapper);
                for (Customer customer : customers) {
                    customerIds.add(customer.getId());
                }
            }
            
            // 2. 创建入住记录查询条件
            QueryWrapper<CheckIn> checkInWrapper = new QueryWrapper<>();
            if (!customerIds.isEmpty()) {
                checkInWrapper.in("customer_id", customerIds);
            } else if ((customerName == null || customerName.isEmpty()) && (customerPhone == null || customerPhone.isEmpty())) {
                // 如果没有传入查询条件，返回空结果
                result.put("code", "200");
                result.put("message", "success");
                result.put("records", new ArrayList<>());
                result.put("total", 0);
                result.put("pageSize", pageSize);
                result.put("pageIndex", pageIndex);
                return result;
            }
            
            // 3. 创建分页对象
            Page<CheckIn> page = new Page<>(pageIndex, pageSize);
            
            // 4. 查询数据
            Page<CheckIn> checkInPage = baseMapper.selectPage(page, checkInWrapper);
            List<CheckIn> checkInList = checkInPage.getRecords();
            
            // 5. 转换为DTO并关联查询客户和房间信息
            List<CheckInDto> checkInDtoList = new ArrayList<>();
            for (CheckIn checkInItem : checkInList) {
                CheckInDto checkInDto = new CheckInDto();
                
                // 设置基本信息
                checkInDto.setCheckInId(checkInItem.getCheckInId());
                checkInDto.setActualCheckIn(checkInItem.getActualCheckIn());
                checkInDto.setActualCheckOut(checkInItem.getActualCheckOut());
                
                // 通过customerId查询客户信息
                if (checkInItem.getCustomerId() != null) {
                    Customer customer = customerMapper.selectById(checkInItem.getCustomerId());
                    if (customer != null) {
                        checkInDto.setCustomerName(customer.getName());
                        checkInDto.setCustomerPhone(customer.getPhone());
                    }
                }
                
                // 通过roomId查询房间信息
                if (checkInItem.getRoomId() != null) {
                    Room room = roomMapper.selectById(checkInItem.getRoomId());
                    if (room != null) {
                        checkInDto.setRoomNumber(room.getRoomNumber());
                    }
                }
                
                checkInDtoList.add(checkInDto);
            }
            
            // 6. 构造返回结果
            result.put("code", "200");
            result.put("message", "success");
            result.put("records", checkInDtoList);
            result.put("total", checkInPage.getTotal());
            result.put("pageSize", pageSize);
            result.put("pageIndex", pageIndex);
            
        } catch (Exception e) {
            result.put("code", "500");
            result.put("message", "查询过程中出现异常：" + e.getMessage());
            result.put("records", new ArrayList<>());
            result.put("total", 0);
            result.put("pageSize", pageSize);
            result.put("pageIndex", pageIndex);
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 直接添加入住记录
     * @param customerName 客户姓名
     * @param customerPhone 客户电话
     * @param roomType 房间类型
     * @param roomNumber 房间编号
     * @return 操作结果
     */
    @Override
    @Transactional
    public Map<String, Object> addCheckIn(String customerName, String customerPhone, String roomType, String roomNumber) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 参数校验
            if (customerName == null || customerName.isEmpty() || customerPhone == null || customerPhone.isEmpty()
                || roomType == null || roomType.isEmpty() || roomNumber == null || roomNumber.isEmpty()) {
                result.put("code", "400");
                result.put("message", "请填写完整的入住信息");
                return result;
            }
            
            // 2. 查找或创建客户
            QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
            customerQueryWrapper.eq("phone", customerPhone);
            Customer customer = customerMapper.selectOne(customerQueryWrapper);
            
            if (customer == null) {
                // 创建新客户
                customer = new Customer();
                customer.setName(customerName);
                customer.setPhone(customerPhone);
                customer.setCustomerId(customer.getPhone());
                customer.setPassword(customerPhone.substring(customerPhone.length() - 6)); // 默认密码为手机号后6位
                customerMapper.insert(customer);
            } else {
                // 验证客户姓名是否匹配
                if (!customer.getName().equals(customerName)) {
                    result.put("code", "400");
                    result.put("message", "客户姓名与手机号不匹配");
                    return result;
                }
            }
            
            // 3. 查找房间
            QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
            roomQueryWrapper.eq("room_number", roomNumber);
            roomQueryWrapper.eq("room_type", roomType);
            Room room = roomMapper.selectOne(roomQueryWrapper);
            
            if (room == null) {
                result.put("code", "400");
                result.put("message", "房间不存在或房间类型不匹配");
                return result;
            }
            
            // 4. 检查房间状态（1表示空闲）
            if (room.getStatus() != 1) {
                result.put("code", "400");
                result.put("message", "房间已被占用");
                return result;
            }
            
            // 5. 创建入住记录
            CheckIn checkIn = new CheckIn();
            checkIn.setCustomerId(customer.getId());
            checkIn.setRoomId(room.getRoomId());
            checkIn.setActualCheckIn(LocalDateTime.now());
            checkIn.setActualCheckOut(null);
            
            boolean saveResult = this.save(checkIn);
            if (!saveResult) {
                result.put("code", "500");
                result.put("message", "创建入住记录失败");
                return result;
            }
            
            // 6. 更新房间状态为已入住（3）
            room.setStatus(3);
            roomMapper.updateById(room);
            
            result.put("code", "200");
            result.put("message", "添加入住记录成功");
        } catch (Exception e) {
            result.put("code", "500");
            result.put("message", "添加入住记录时发生异常：" + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
}
