package com.wrx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wrx.dto.LoginUserDto;
import com.wrx.dto.EmployeeDto;
import com.wrx.entity.Role;
import com.wrx.entity.EmployeeRole;
import com.wrx.entity.Employee;
import com.wrx.mapper.EmployeeMapper;
import com.wrx.service.IRoleService;
import com.wrx.service.IEmployeeRoleService;
import com.wrx.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wrx.util.JwtUtil;
import com.wrx.util.LoginEmployee;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王荣星
 * @since 2025-04-25
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {



    //分页查询user（所有或根据条件）
    @Override
    public Page<Employee> selectByPage(Employee employee, int pageIndex, int pageSize) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>();
        if (StringUtils.isNotBlank(employee.getEmployeeId()))
            wrapper.eq(Employee::getEmployeeId, employee.getEmployeeId());
        if (StringUtils.isNotBlank(employee.getName()))
            wrapper.eq(Employee::getName, employee.getName());
        Page<Employee> userPage = new Page<>(pageIndex,pageSize);
        return this.page(userPage, wrapper);
    }
    /*
    1、分页查询user（所有或根据条件）
    2、根据user_id查询SysUserRole
    3、根据SysUserRole的role_id查询Role
     */
    @Resource
    private IEmployeeRoleService sysUserRoleService;
    @Resource
    private IRoleService sysRoleService;
    @Override
    public Map<String, Object> selectUserAndRole(Employee employee, int pageIndex, int pageSize) {
        //分页查询user（所有或根据条件）
        Page<Employee> userPage = this.selectByPage(employee,pageIndex,pageSize);
        List<Employee> employeeList = userPage.getRecords();
        //遍历userList，并查询每个user的角色，重新封装成UserDto的链表
        ArrayList<EmployeeDto> employeeDtoList = new ArrayList<>();
        if(!employeeList.isEmpty()){
            for (Employee u : employeeList) {
                EmployeeDto employeeDto = new EmployeeDto();
                BeanUtils.copyProperties(u, employeeDto);
                //根据user_id查询SysUserRole
                List<Role> roleList = this.selectRolesByUserId(u);
                //把角色链表存入userDto
                employeeDto.setRoleList(roleList);
                //把每一个生成的userDto存入链表
                employeeDtoList.add(employeeDto);
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", employeeDtoList);
        map.put("total",userPage.getTotal());
        map.put("pages",userPage.getPages());
        return map;
    }
    //更新权限
    @Transactional //事务注解
    @Override
    public boolean updateUserRole(EmployeeDto employeeDto) {
        //根据user_id删除user对应的权限
        boolean b1 = sysUserRoleService.removeRoleService(employeeDto.getId());
        //新增选择的权限
        ArrayList<EmployeeRole> employeeRoleList = new ArrayList<>();
        for(Role role : employeeDto.getRoleList()){
            EmployeeRole employeeRole = new EmployeeRole();
            employeeRole.setRoleId(role.getRoleId());
            employeeRole.setEmployeeId(employeeDto.getId());
            employeeRoleList.add(employeeRole);
        }
        //批量把sysUserRoleList添加
        boolean b2 = sysUserRoleService.saveBatch(employeeRoleList);
        return b1 || b2;
    }
    //教师管理
    @Override
    public Map<String, Object> selectAllUser(Employee employee, int pageIndex, int pageSize) {
        Page<Employee> userPage = this.selectByPage(employee,pageIndex,pageSize);
        HashMap<String, Object> map = new HashMap<>();
        map.put("records",userPage.getRecords());
        map.put("total",userPage.getTotal());
        map.put("pages",userPage.getPages());
        return map;
    }
    //TODO 权限管理-批量授权
    @Override
    public boolean updateUserinitialPerminssion() {
        LambdaQueryWrapper<Employee> Userid = new LambdaQueryWrapper<>();
        Userid.select(Employee::getId);
        LambdaQueryWrapper<EmployeeRole> sysUserid = new LambdaQueryWrapper<>();
        sysUserid.select(EmployeeRole::getEmployeeId);

        return false;
    }

    //根据user查询role
    @Override
    public List<Role> selectRolesByUserId(Employee employee) {
        //根据user_id查询EmployeeRole
        List<EmployeeRole> employeeRoleList = sysUserRoleService.selectByUserId(employee.getId());
        ArrayList<Role> roleList = new ArrayList<>();
        if(!employeeRoleList.isEmpty()){
            for (EmployeeRole employeeRole : employeeRoleList) {
                Role role = sysRoleService.getById(employeeRole.getRoleId());//查询角色
                roleList.add(role);
            }
        }
        return roleList;
    }

    @Resource
    private AuthenticationManager authenticationManager;
    @Override
    public LoginUserDto login(Employee employee) {
        //判断获取的 UserDetails 信息中的密码是否和前端提交的密码一致，
        //security调用AuthenticationManager进行认证
        // 如果一致返回一个通过验证了的 UserDetails（LoginUser），如果密码不一致抛出异常
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(employee.getEmployeeId(), employee.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //把合法的authentication的内容处理或封装到LoginUserDto
        LoginEmployee loginEmployee = (LoginEmployee) authentication.getPrincipal();
        //密码验证成功，根据 user 的 id 查询对应的 SysRole 信息并存入 loginUser
        List<Role> roleList = this.selectRolesByUserId(loginEmployee.getEmployee());
        List<String> roles = null;
        //sysRoleList 表中取出角色信息{"ROLE_teacher","ROLE_edu_admin"}
        if (roleList != null && roleList.size() > 0) {
            roles = new ArrayList<>();
            for (Role role : roleList) {
                roles.add(role.getRoleKey());
            }
            //roles = sysRoleList.stream().map(role -> role.getRoleKey()).collect(Collectors.toList());
            //loginUser.setPermissions(roles);
        }
        //TODO 把user及角色信息存入redis
        LoginUserDto loginUserDto = new LoginUserDto();
        //复制Employee，粘贴到LoginEmployeeDto
        BeanUtil.copyProperties(loginEmployee.getEmployee(), loginUserDto, true);
        String token = JwtUtil.creatToken(loginEmployee.getEmployee().getId());
        loginUserDto.setToken(token);
        loginUserDto.setRoles(roles);
        return loginUserDto;
    }
}
