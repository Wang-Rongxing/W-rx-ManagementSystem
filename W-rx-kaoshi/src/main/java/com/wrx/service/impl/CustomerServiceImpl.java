package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wrx.dto.CheckInDto;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.CheckIn;
import com.wrx.entity.Customer;
import com.wrx.entity.Room;
import com.wrx.mapper.CheckInMapper;
import com.wrx.mapper.CustomerMapper;
import com.wrx.mapper.RoomMapper;
import com.wrx.service.ICustomerService;
import com.wrx.util.JwtUtil;
import com.wrx.util.LoginCustomer;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-10-28
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private CheckInMapper checkInMapper;
    @Resource
    private RoomMapper roomMapper;
    @Override
    public LoginUserDto login(Customer customer) {
        String usernameWithRole = "customer:" + customer.getCustomerId();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameWithRole, customer.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        LoginCustomer loginCustomer = (LoginCustomer) authentication.getPrincipal();
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setName(loginCustomer.getCustomer().getName());
        loginUserDto.setEmployeeId(loginCustomer.getCustomer().getCustomerId());
        String token = JwtUtil.creatToken(loginCustomer.getCustomer().getId());
        loginUserDto.setToken(token);
        List<String> roles = new ArrayList<>();
        roles.add("Customer");
        loginUserDto.setRoles(roles);
        return loginUserDto;
    }

    @Override
    public boolean register(Customer customer) {
        // 检查账号是否已存在
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getCustomerId, customer.getCustomerId());
        Customer existingCustomer = baseMapper.selectOne(queryWrapper);
        if (existingCustomer != null) {
            // 账号已存在，注册失败
            return false;
        }
        
        // 检查手机号是否已被使用
        LambdaQueryWrapper<Customer> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(Customer::getPhone, customer.getPhone());
        existingCustomer = baseMapper.selectOne(phoneWrapper);
        if (existingCustomer != null) {
            // 手机号已被使用，注册失败
            return false;
        }
        
        // 对密码进行加密
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        
        // 保存客户信息
        return save(customer);
    }

    @Override
    public Page<Customer> selectByPage(Customer customer, int pageNum, int pageSize) {
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<Customer>();
        if (StringUtils.isNotBlank(customer.getCustomerId()))
            wrapper.like(Customer::getCustomerId, customer.getCustomerId());
        if (StringUtils.isNotBlank(customer.getName()))
            wrapper.like(Customer::getName, customer.getName());
        Page<Customer> userPage = new Page<>(pageNum,pageSize);
        return this.page(userPage, wrapper);
    }

    @Override
    public Map<String, Object> selectAllUser(Customer customer, int pageIndex, int pageSize) {
        Page<Customer> userPage = this.selectByPage(customer,pageIndex,pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",userPage.getRecords());
        map.put("total",userPage.getTotal());
        map.put("pages",userPage.getPages());
        return map;
    }

    @Override
    public Map<String, Object> selectCustomerByIdOrName(Customer customer) {
        // 创建查询条件构造器
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        
        // 根据customerId进行精确查询
        if (StringUtils.isNotBlank(customer.getCustomerId())) {
            wrapper.eq(Customer::getCustomerId, customer.getCustomerId());
        }
        
        // 根据name进行模糊查询
        if (StringUtils.isNotBlank(customer.getName())) {
            wrapper.like(Customer::getName, customer.getName());
        }
        
        // 执行查询获取符合条件的客户列表
        List<Customer> customerList = this.list(wrapper);
        
        // 构建返回结果
        HashMap<String, Object> result = new HashMap<>();
        result.put("records", customerList);
        result.put("total", customerList.size());
        result.put("pages", customerList.isEmpty() ? 0 : 1);
        
        return result;
    }
    
    @Override
    public boolean insertUser(Customer customer) {
        
        // 检查入参
        if (customer == null || customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
            System.out.println("客户ID为空");
            return false;
        }
        
        // 检查客户ID是否已存在
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Customer::getCustomerId, customer.getCustomerId());
        Customer existingCustomer = this.getOne(wrapper);
        if (existingCustomer != null) {
            System.out.println("客户ID已存在: " + customer.getCustomerId());
            return false;
        }

        
        // 对密码进行加密
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        
        // 保存客户信息
        return this.save(customer);
    }
    
    @Override
    public Customer getCustomerById(String customerId) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getCustomerId, customerId);
        return this.getOne(queryWrapper);
    }

    @Override
    public Customer getCustomerById(Integer Id) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getId, Id);
        return this.getOne(queryWrapper);
    }

    @Override
    public Map<String, Object> getHistory(String customerId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 根据customerId查询客户信息，获取数据库主键ID
            QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
            customerQueryWrapper.eq("customer_id", customerId);
            Customer customer = baseMapper.selectOne(customerQueryWrapper);
            
            if (customer == null) {
                result.put("success", false);
                result.put("message", "客户不存在");
                return result;
            }
            
            // 创建查询条件，根据客户的数据库ID查询入住记录
            QueryWrapper<CheckIn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("customer_id", customer.getId());
            queryWrapper.orderByDesc("actual_check_in"); // 按入住时间倒序
            
            // 创建分页对象
            Page<CheckIn> pageObj = new Page<>(page, pageSize);
            
            // 执行分页查询
            Page<CheckIn> checkInPage = checkInMapper.selectPage(pageObj, queryWrapper);
            List<CheckIn> checkInList = checkInPage.getRecords();
            
            // 转换为DTO并关联查询房间信息
            List<CheckInDto> checkInDtoList = new ArrayList<>();
            for (CheckIn checkIn : checkInList) {
                CheckInDto checkInDto = new CheckInDto();
                
                // 设置基本信息
                checkInDto.setCheckInId(checkIn.getCheckInId());
                checkInDto.setActualCheckIn(checkIn.getActualCheckIn());
                checkInDto.setActualCheckOut(checkIn.getActualCheckOut());
                checkInDto.setCustomerName(customer.getName());
                checkInDto.setCustomerPhone(customer.getPhone());
                
                // 通过roomId查询房间信息
                if (checkIn.getRoomId() != null) {
                    Room room = roomMapper.selectById(checkIn.getRoomId());
                    if (room != null) {
                        checkInDto.setRoomType(room.getRoomType());
                        checkInDto.setRoomNumber(room.getRoomNumber());
                    }
                }
                
                checkInDtoList.add(checkInDto);
            }
            
            // 构造返回结果
            result.put("records", checkInDtoList);
            result.put("total", checkInPage.getTotal());
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
}
