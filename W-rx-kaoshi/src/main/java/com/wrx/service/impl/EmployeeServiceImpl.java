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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    
    /**
     * 根据id删除员工及其对应的EmployeeRole数据
     * @param id 员工ID
     * @return 是否删除成功
     */
    @Transactional
    public boolean deleteEmployeeAndRole(Integer id) {
        if (id == null || id <= 0) {
            return false;
        }
        try {
            // 先删除对应的EmployeeRole数据
            sysUserRoleService.removeRoleService(id);
            // 再删除员工数据
            return this.removeById(id);
        } catch (Exception e) {
            // 可以添加日志记录
            return false;
        }
    }



    //分页查询user（所有或根据条件）
    @Override
    public Page<Employee> selectByPage(Employee employee, int pageIndex, int pageSize) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>();
        if (StringUtils.isNotBlank(employee.getEmployeeId()))
            wrapper.like(Employee::getEmployeeId, employee.getEmployeeId());
        if (StringUtils.isNotBlank(employee.getName()))
            wrapper.like(Employee::getName, employee.getName());
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
    @Resource
    private PasswordEncoder passwordEncoder;
    @Override
    public LoginUserDto login(Employee employee) {
        //判断获取的 UserDetails 信息中的密码是否和前端提交的密码一致，
        //security调用AuthenticationManager进行认证
        // 如果一致返回一个通过验证了的 UserDetails（LoginUser），如果密码不一致抛出异常
        // 将角色信息包含在username中，格式：role:username
        String usernameWithRole = "hotel:" + employee.getEmployeeId();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usernameWithRole, employee.getPassword());
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
    
    @Override
    public boolean resetUserPassword(Integer id, String employeeId) {
        // 根据ID和工号查询员工，确保数据一致性
        Employee employee = this.getById(id);
        if (employee == null || !employee.getEmployeeId().equals(employeeId)) {
            return false;
        }
        
        // 设置默认密码
        String defaultPassword = "wrx123456";
        
        // 对默认密码进行加密
        String encodedPassword = passwordEncoder.encode(defaultPassword);
        
        // 更新密码
        employee.setPassword(encodedPassword);
        return this.updateById(employee);
    }
    
    @Override
    public boolean insertUser(Employee employee) {
        // 检查工号是否已存在
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getEmployeeId, employee.getEmployeeId());
        Employee existingEmployee = this.getOne(wrapper);
        if (existingEmployee != null) {
            // 工号已存在，返回false
            return false;
        }
        
        // 对密码进行加密
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);
        
        // 设置创建时间（如果有相应字段）
        // employee.setCreateTime(new Date());
        
        // 保存员工信息
        return this.save(employee);
    }
    
    @Override
    public Map<String, Object> selectEmployeeByIdOrName(Employee employee) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        
        // 根据工号或用户名进行搜索
        if (StringUtils.isNotBlank(employee.getEmployeeId())) {
            wrapper.like(Employee::getEmployeeId, employee.getEmployeeId());
        }
        if (StringUtils.isNotBlank(employee.getName())) {
            wrapper.like(Employee::getName, employee.getName());
        }
        
        // 查询数据
        List<Employee> employeeList = this.list(wrapper);
        
        // 为每个员工查询角色信息
        List<Map<String, Object>> recordsWithRoles = new ArrayList<>();
        for (Employee emp : employeeList) {
            Map<String, Object> employeeWithRoles = BeanUtil.beanToMap(emp, new HashMap<>(), false, true);
            // 查询该员工的角色列表
            List<Role> roleList = this.selectRolesByUserId(emp);
            employeeWithRoles.put("roleList", roleList);
            recordsWithRoles.add(employeeWithRoles);
        }
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", recordsWithRoles);
        result.put("total", recordsWithRoles.size());
        
        return result;
    }
}
