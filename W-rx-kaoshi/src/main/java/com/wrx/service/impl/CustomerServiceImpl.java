package com.wrx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.dto.LoginUserDto;
import com.wrx.entity.Customer;
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
import java.util.List;

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
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customer.getCustomerId(), customer.getPassword());
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
}
