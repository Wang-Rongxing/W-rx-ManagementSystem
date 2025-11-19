package com.wrx.filter;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wrx.entity.Customer;
import com.wrx.entity.Employee;
import com.wrx.entity.Role;
import com.wrx.entity.SysUser;
import com.wrx.exception.NoRolesException;
import com.wrx.service.ICustomerService;
import com.wrx.service.IEmployeeService;
import com.wrx.service.IRoleService;
import com.wrx.service.ISysUserService;
import com.wrx.util.JwtUtil;
import com.wrx.util.LoginCustomer;
import com.wrx.util.LoginEmployee;
import com.wrx.util.LoginSysUSer;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/*
*获取前端提交的jwt，并对jwt解析，从而获取user的角色，然后让Security框架鉴权
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    @Lazy(value = true)//避免循环调用，延迟加载，用到懒加载
    private IEmployeeService iEmployeeService;
    @Resource
    @Lazy(value = true)//避免循环调用，延迟加载，用到懒加载
    private ISysUserService iSysUserService;
    @Resource
    @Lazy(value = true)//避免循环调用，延迟加载，用到懒加载
    private IRoleService iRoleService;
    
    @Resource
    @Lazy(value = true)//避免循环调用，延迟加载，用到懒加载
    private ICustomerService iCustomerService;
    //由 handlerExceptionResolver 引入全局异常
    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //从request中获取token（jwt）
        //从jwt的负载中获取userId
        //通过userId去数据库中查询user对应的角色
        //把角色信息封装到Authentication
        //把合法带有角色的Authentication带入本次Security框架，由Security框架进行鉴权
        if (request.getServletPath().equals("/employee/login")||request.getServletPath().equals("/sysuser/login")||request.getServletPath().equals("/customer/login")
                ||request.getServletPath().equals("/customer/register")) {
            // 放行
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = request.getHeader("token");
            if(StrUtil.isBlank(token)){//判断jwt是否为空
                throw new NoRolesException("没有操作权限，请登录");
            }
            // 验证 token
            JwtUtil.checkToken(token);//检验token的合法性（是否被篡改、过期），抛出异常，由全局异常处理器来处理
            //从 token 中取出 json 格式的 user 和 roles
            DecodedJWT decode = JWT.decode(token);
            Claim claim = decode.getClaim("userId");
            Long userId = claim.asLong();
            //通过userId去数据库中查询user对应的角色
            //TODO 从redis（内存）中获取user对应的角色，高频访问的数据存入redis
            
            // 尝试从Employee表获取用户信息
            Employee employee = iEmployeeService.getById(Math.toIntExact(userId));
            if (employee != null) {
                List<Role> roleList = iEmployeeService.selectRolesByUserId(employee);
                //从角色链表中获取Role_key形成List<String>:{"ROLE_teacher","ROLE_edu_admin"}
                List<String> roles = roleList.stream().map(role -> role.getRoleKey()).collect(Collectors.toList());

                LoginEmployee loginEmployee = new LoginEmployee();
                loginEmployee.setEmployee(employee);
                loginEmployee.setPermissions(roles);

                // 把 usernamePasswordAuthenticationToken 设置到 security 框架
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        loginEmployee, null, loginEmployee.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                //token 快要过期的处理
                handleTokenRenewal(response, decode, employee, roles);
            } else {
                // 尝试从SysUser表获取用户信息
                SysUser sysUser = iSysUserService.getById(Math.toIntExact(userId));
                if (sysUser != null) {
                    List<String> sysuserroles = new ArrayList<>();
                    sysuserroles.add(sysUser.getRole());
                    LoginSysUSer loginSysUSer = new LoginSysUSer();
                    loginSysUSer.setSysUser(sysUser);
                    loginSysUSer.setPermissions(sysuserroles);

                    // 把 usernamePasswordAuthenticationToken 设置到 security 框架
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            loginSysUSer, null, loginSysUSer.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    //token 快要过期的处理
                    handleTokenRenewal(response, decode, sysUser, sysuserroles);
                } else {
                    // 尝试从Customer表获取用户信息
                    Customer customer = iCustomerService.getById(Math.toIntExact(userId));
                    if (customer == null) {
                        // 如果所有表都找不到用户，抛出异常
                        throw new NoRolesException("用户不存在");
                    }
                    List<String> customerroles = new ArrayList<>();
                    customerroles.add("Customer");
                    LoginCustomer loginCustomer = new LoginCustomer();
                    loginCustomer.setCustomer(customer);
                    loginCustomer.setPermissions(customerroles);

                    // 把 usernamePasswordAuthenticationToken 设置到 security 框架
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            loginCustomer, null, loginCustomer.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    //token 快要过期的处理
                    handleTokenRenewal(response, decode, customer, customerroles);
                }
            }
            // 放行
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
    
    /**
     * 处理token续签逻辑
     */
    private void handleTokenRenewal(HttpServletResponse response, DecodedJWT decode, Object user, List<String> roles) {
        //token 快要过期的处理
        Claim exp = decode.getClaim("exp");
        Date date = exp.asDate();
        // long between = DateUtil.betweenMs(date, new Date());
        long between = date.getTime() - new Date().getTime();
        long time = Convert.convertTime(between, TimeUnit.MILLISECONDS, TimeUnit.MINUTES);
        // 剩余时间低于 30 分钟，如果还在访问就续签（重新派发一个新的 token,继续获得有效期），需要前端配合检查响应头中是否
        //有 token，有 token 则重新存入 SessionStorage
        if (time <= 30) {
            String newToken = null;
            // 根据用户类型调用相应的JwtUtil方法
            if (user instanceof Employee) {
                newToken = JwtUtil.creatToken((Employee) user, roles);
            } else if (user instanceof SysUser) {
                newToken = JwtUtil.creatToken((SysUser) user, roles);
            } else if (user instanceof Customer) {
                newToken = JwtUtil.creatToken((Customer) user, roles);
            }
            
            if (newToken != null) {
                response.setHeader("token", newToken);
                response.setHeader("Access-Control-Expose-Headers", "token");// 跨域
            }
        }
    }
}
