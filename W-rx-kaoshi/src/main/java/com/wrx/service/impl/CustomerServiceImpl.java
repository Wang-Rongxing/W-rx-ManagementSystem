package com.wrx.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
import com.wrx.entity.Employee;
import com.wrx.mapper.CustomerMapper;
import com.wrx.service.ICustomerService;
import com.wrx.util.JwtUtil;
import com.wrx.util.LoginCustomer;
import com.wrx.util.LoginSysUSer;
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
    @Override
    public LoginUserDto login(Customer customer) {
        //判断获取的 UserDetails 信息中的密码是否和前端提交的密码一致，
        //security调用AuthenticationManager进行认证
        // 如果一致返回一个通过验证了的 UserDetails（LoginUser），如果密码不一致抛出异常
        // 将角色信息包含在username中，格式：role:username
        String usernameWithRole = "customer:" + customer.getCustomerId();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameWithRole, customer.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //把合法的authentication的内容处理或封装到LoginUserDto
        LoginCustomer loginCustomer = (LoginCustomer) authentication.getPrincipal();
        //密码验证成功，根据 user 的 id 查询对应的 SysRole 信息并存入 loginUser

        //TODO 把user及角色信息存入redis
        LoginUserDto loginUserDto = new LoginUserDto();
        //复制Employee，粘贴到LoginEmployeeDto
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
}
